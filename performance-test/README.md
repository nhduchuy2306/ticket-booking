# Performance Test Suite

Bộ test hiệu năng này dùng **K6 + TypeScript** để mô phỏng luồng người dùng khi một event được **on sale** và có rất nhiều người truy cập cùng lúc.

## Mục tiêu

- Đo khả năng chịu tải của hệ thống khi lượng user tăng đột biến.
- Mô phỏng luồng thực tế: vào danh sách event on sale → mở chi tiết event → xem thêm danh sách event sắp diễn ra.
- Tự động lấy access token từ `auth-service` bằng `customer/auth/login`, cache theo VU và tự refresh khi cần.
- Hỗ trợ chạy các mức tải **10,000 / 20,000 / 30,000 VU** bằng các script riêng hoặc override bằng env.

## Cấu trúc thư mục

```text
performance-test/
  package.json
  tsconfig.json
  README.md
  .env.example
  .gitignore
  data/
    .gitkeep
  reports/
    .gitkeep
  scripts/
    event-on-sale.smoke.ts
    event-on-sale.load.ts
    event-on-sale.stress.ts
    event-on-sale.spike.ts
  src/
    config.ts
    helpers.ts
    event-on-sale.ts
    auth/
      customer-auth.ts
  types/
    k6.d.ts
  dist/  (generated)
```

## Flow test chính

1. `GET /events/on-sale`
2. `POST /auths/customer/auth/login` để lấy token tự động từ `auth-service` nếu `AUTH_ENABLED=true`
3. Trích `eventId` từ response hoặc dùng `EVENT_ID` nếu bạn muốn cố định dữ liệu
4. `GET /events/{eventId}`
5. `GET /events/coming-events` nếu bật `BROWSE_COMING_EVENTS=true`

> Mình ưu tiên các endpoint public để đo đúng trải nghiệm browse của người dùng khi event vừa mở bán.

## Yêu cầu trước khi chạy

- Chạy được API Gateway ở `http://localhost:9999`
- Eureka, event-service và các service liên quan đã khởi động
- Có ít nhất 1 event đang on sale trong dữ liệu test, hoặc cung cấp `EVENT_ID`

## Cài đặt

Chạy `npm install` trong `performance-test/` để cài TypeScript và type definitions phục vụ bước build.

> Script K6 được viết bằng TypeScript và sẽ được build sang `dist/` trước khi chạy.

## Cách chạy

### Smoke test

```bash
cd performance-test
npm run smoke
```

### Load test 10k user

```bash
cd performance-test
npm run load
```

### Stress test 20k user

```bash
cd performance-test
npm run stress
```

### Spike test 30k user

```bash
cd performance-test
npm run spike
```

## Chạy bằng Docker Compose

Nếu bạn muốn chạy K6 ngay trong Docker Compose của dự án, service `k6` đã được thêm vào `infrastructure/dockers/docker-compose.yml`.

### Build image K6

```bash
cd infrastructure/dockers
docker compose --profile performance build k6
```

### Chạy load test mặc định

```bash
cd infrastructure/dockers
docker compose --profile performance run --rm k6
```

### Chạy spike 30k user

```bash
cd infrastructure/dockers
docker compose --profile performance run --rm k6 run dist/scripts/event-on-sale.spike.js
```

### Ghi report ra host

```bash
cd infrastructure/dockers
docker compose --profile performance run --rm k6 run --out json=/tests/reports/event-on-sale-load.json dist/scripts/event-on-sale.load.js
```

## Override nhanh theo nhu cầu

Bạn có thể đổi số VU và thời lượng ngay từ command line:

```bash
npm run build
k6 run -e BASE_URL=http://localhost:9999 -e VUS=30000 -e DURATION=10m dist/scripts/event-on-sale.spike.js
```

Nếu response của `/events/on-sale` có dữ liệu chuẩn, script sẽ tự lấy `eventId`. Nếu không, hãy set:

```bash
k6 run -e BASE_URL=http://localhost:9999 -e EVENT_ID=<real-event-id> dist/scripts/event-on-sale.load.js
```

## Biến môi trường hỗ trợ

| Biến | Mô tả |
| --- | --- |
| `BASE_URL` | URL của API Gateway, mặc định `http://localhost:9999` |
| `EVENT_ID` | Event ID fallback nếu không trích được từ response |
| `AUTH_ENABLED` | Bật/tắt tự động login customer (mặc định `true`) |
| `AUTH_LOGIN_EMAIL` | Email customer dùng để login |
| `AUTH_LOGIN_PASSWORD` | Password customer dùng để login |
| `AUTH_LOGIN_PATH` | Đường dẫn login customer qua gateway |
| `AUTH_REFRESH_PATH` | Đường dẫn refresh token customer qua gateway |
| `AUTH_REFRESH_THRESHOLD_SECONDS` | Chủ động refresh trước khi token hết hạn |
| `AUTH_TOKEN` | Bearer token fallback nếu muốn bỏ qua login |
| `AUTH_HEADER` | Tên header auth, mặc định `Authorization` |
| `THINK_TIME` | Nghỉ giữa mỗi vòng lặp (giây) |
| `BROWSE_COMING_EVENTS` | `true/false`, có gọi `/events/coming-events` hay không |
| `INSECURE_SKIP_TLS_VERIFY` | Bỏ verify TLS khi test môi trường non-local |
| `VUS` | Override số virtual users của profile |
| `DURATION` | Override thời gian chạy của profile |

## Auth flow

Khi `AUTH_ENABLED=true`, script sẽ:

1. Gọi `POST /auths/customer/auth/login`
2. Lưu `accessToken` và `refreshToken` theo từng VU
3. Tự gắn `Authorization: Bearer <token>` vào request
4. Tự refresh token qua `POST /auths/customer/auth/refresh-token` khi gần hết hạn

Điều này giúp test phản ánh gần hơn hành vi của user đã đăng nhập khi mở bán event.

## Khuyến nghị vận hành

- Với **10k+ VU**, nên chạy phân tán trên nhiều máy hoặc dùng K6 Cloud.
- Đừng chạy 30k VU trên một máy dev đơn lẻ nếu muốn kết quả đáng tin cậy.
- Đảm bảo có đủ dữ liệu on-sale thật trước khi bấm test, nếu không script sẽ dừng tại bước lấy `eventId`.
- Với auth bật, hãy chuẩn bị một tài khoản customer ổn định cho test để tránh tạo nhiễu dữ liệu.

## Lưu kết quả

Bạn có thể xuất report ra folder `reports/` bằng K6 CLI:

```bash
k6 run --out json=reports/event-on-sale-load.json dist/scripts/event-on-sale.load.js
```

