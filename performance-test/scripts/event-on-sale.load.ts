import { createEventOnSaleOptions, runEventOnSaleJourney } from '../src/event-on-sale.js';
import { envNumber, envString } from '../src/config.js';

export const options = createEventOnSaleOptions({
  profile: 'event-on-sale-load',
  vus: envNumber('VUS', 10000),
  duration: envString('DURATION', '15m'),
  thresholds: {
    checks: ['rate>0.99'],
    http_req_duration: ['p(95)<1500', 'p(99)<3000'],
  },
});

// noinspection JSUnusedGlobalSymbols
export default function eventOnSaleLoadScenario(): void {
  runEventOnSaleJourney();
}

