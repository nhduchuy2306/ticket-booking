import { createEventOnSaleOptions, runEventOnSaleJourney } from '../src/event-on-sale.js';
import { envNumber, envString } from '../src/config.js';

export const options = createEventOnSaleOptions({
  profile: 'event-on-sale-spike',
  vus: envNumber('VUS', 30000),
  duration: envString('DURATION', '5m'),
  thresholds: {
    checks: ['rate>0.97'],
    http_req_duration: ['p(95)<4000', 'p(99)<7000'],
  },
});

// noinspection JSUnusedGlobalSymbols
export default function eventOnSaleSpikeScenario(): void {
  runEventOnSaleJourney();
}

