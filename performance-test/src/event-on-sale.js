import http from 'k6/http';
import { check, fail, sleep } from 'k6';

import { envBoolean, envNumber, envString, getBaseUrl, getHeaders } from './config.js';
import { pickEventId, safeJson } from './helpers.js';

const DEFAULT_THRESHOLDS = {
  checks: ['rate>0.98'],
  http_req_failed: ['rate<0.01'],
  http_req_duration: ['p(95)<2000', 'p(99)<5000'],
};

let cachedEventId = '';

function buildUrl(baseUrl, path) {
  return `${baseUrl}${path}`;
}

function makeRequest(baseUrl, path, tags) {
  return http.get(buildUrl(baseUrl, path), {
    headers: getHeaders(),
    tags,
  });
}

function assertJsonResponse(response, label) {
  return check(response, {
    [`${label} status is successful`]: (res) => res.status >= 200 && res.status < 300,
    [`${label} has response body`]: (res) => typeof res.body === 'string' && res.body.length > 0,
  });
}

export function createEventOnSaleOptions({
  profile,
  vus,
  duration,
  thresholds = {},
  gracefulStop = '30s',
} = {}) {
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

export function runEventOnSaleJourney() {
  const baseUrl = getBaseUrl();
  const thinkTime = envNumber('THINK_TIME', 1);
  const browseComingEvents = envBoolean('BROWSE_COMING_EVENTS', true);

  const onSaleResponse = makeRequest(baseUrl, '/events/on-sale', {
    endpoint: 'events-on-sale',
    flow: 'event-on-sale',
  });

  assertJsonResponse(onSaleResponse, 'on-sale list');

  const onSalePayload = safeJson(onSaleResponse);
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

