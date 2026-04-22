FROM node:22-alpine AS build
WORKDIR /app

COPY package.json tsconfig.json ./
COPY src ./src
COPY scripts ./scripts
COPY types ./types
COPY k6 ./k6

RUN npm install && npm run build

FROM grafana/k6:0.56.0
WORKDIR /tests

COPY --from=build /app/dist ./dist

ENTRYPOINT ["k6"]
CMD ["run", "dist/scripts/event-on-sale.smoke.js"]

