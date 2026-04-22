import { createEventOnSaleOptions, runEventOnSaleJourney } from '../src/event-on-sale.js';
import { envNumber, envString } from '../src/config.js';

export const options = createEventOnSaleOptions({
  profile: 'event-on-sale-stress',
  vus: envNumber('VUS', 20000),
  duration: envString('DURATION', '10m'),
  thresholds: {
    checks: ['rate>0.98'],
    http_req_duration: ['p(95)<2500', 'p(99)<5000'],
  },
});

// noinspection JSUnusedGlobalSymbols
export default function eventOnSaleStressScenario(): void {
  runEventOnSaleJourney();
}

