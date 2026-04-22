import { createEventOnSaleOptions, runEventOnSaleJourney } from '../src/event-on-sale.js';
import { envNumber, envString } from '../src/config.js';

export const options = createEventOnSaleOptions({
  profile: 'event-on-sale-smoke',
  vus: envNumber('VUS', 20),
  duration: envString('DURATION', '2m'),
  thresholds: {
    checks: ['rate>0.99'],
    http_req_duration: ['p(95)<750', 'p(99)<1500'],
  },
});

// noinspection JSUnusedGlobalSymbols
export default function eventOnSaleSmokeScenario(): void {
  runEventOnSaleJourney();
}

