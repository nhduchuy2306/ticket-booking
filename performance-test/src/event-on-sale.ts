import http from 'k6/http';
import { check, fail, sleep } from 'k6';

import { envBoolean, envNumber, envString, getBaseUrl } from './config.js';
import { getCustomerAuthHeaders } from './auth/customer-auth.js';
import { pickEventId, safeJson } from './helpers.js';

export type EventOnSaleScenarioOptions = {
  profile: string;
  vus: number;
  duration: string;
  thresholds?: Record<string, string[]>;
  gracefulStop?: string;
};

const DEFAULT_THRESHOLDS: Record<string, string[]> = {
  checks: ['rate>0.98'],
  http_req_failed: ['rate<0.01'],
  http_req_duration: ['p(95)<2000', 'p(99)<5000'],
};

let cachedEventId = '';

function buildUrl(baseUrl: string, path: string): string {
  return `${baseUrl}${path}`;
}

function makeRequest(baseUrl: string, path: string, tags: Record<string, string>): http.RefinedResponse {
  return http.get(buildUrl(baseUrl, path), {
    headers: {
      Accept: 'application/json',
      ...getCustomerAuthHeaders(),
    },
    tags,
  });
}

function assertJsonResponse(response: http.RefinedResponse, label: string): void {
  check(response, {
    [`${label} status is successful`]: (res: { status: number }) => res.status >= 200 && res.status < 300,
    [`${label} has response body`]: (res: { body?: string }) => typeof res.body === 'string' && res.body.length > 0,
  });
}

export function createEventOnSaleOptions({
  profile,
  vus,
  duration,
  thresholds = {},
  gracefulStop = '30s',
}: EventOnSaleScenarioOptions): Record<string, unknown> {
  return {
    scenarios: {
      [profile]: {
        executor: 'constant-vus',
        vus,
        duration,
        gracefulStop,
        exec: 'default',
        tags: {
          flow: 'event-on-sale',
          profile,
        },
      },
    },
    thresholds: {
      ...DEFAULT_THRESHOLDS,
      ...thresholds,
    },
    discardResponseBodies: false,
  };
}

export function runEventOnSaleJourney(): void {
  const baseUrl = getBaseUrl();
  const thinkTime = envNumber('THINK_TIME', 1);
  const browseComingEvents = envBoolean('BROWSE_COMING_EVENTS', true);

  const onSaleResponse = makeRequest(baseUrl, '/events/on-sale', {
    endpoint: 'events-on-sale',
    flow: 'event-on-sale',
  });

  assertJsonResponse(onSaleResponse, 'on-sale list');

  const onSalePayload = safeJson(onSaleResponse.body as string | undefined);
  const discoveredEventId = pickEventId(onSalePayload, envString('EVENT_ID', ''));
  if (discoveredEventId) {
    cachedEventId = discoveredEventId;
  }

  const eventId = cachedEventId || envString('EVENT_ID', '');
  if (!eventId) {
    fail('Không tìm thấy eventId từ /events/on-sale. Hãy seed dữ liệu on-sale hoặc set EVENT_ID.');
  }

  const detailResponse = makeRequest(baseUrl, `/events/${encodeURIComponent(eventId)}`, {
    endpoint: 'event-detail',
    event_id: eventId,
    flow: 'event-on-sale',
  });

  assertJsonResponse(detailResponse, 'event detail');

  if (browseComingEvents) {
    const comingEventsResponse = makeRequest(baseUrl, '/events/coming-events', {
      endpoint: 'coming-events',
      flow: 'event-on-sale',
    });

    assertJsonResponse(comingEventsResponse, 'coming events list');
  }

  sleep(thinkTime);
}

