## 🎯 **Main Flow từ Organizer tạo tài khoản đến khi Customer thanh toán thành công**

---

### **I. Organizer đăng ký và tạo sự kiện**

#### 🟠 **1. Organizer đăng ký tài khoản**

* 📍 **Auth Service**

    * Organizer gửi thông tin đăng ký → Tạo tài khoản Organizer
    * Nhận token đăng nhập (JWT)

#### 🟠 **2. Organizer đăng nhập & tạo sự kiện**

* 📍 **Auth Service**

    * Đăng nhập → Lấy token xác thực
* 📍 **Event Service**

    * Gửi yêu cầu tạo sự kiện (thông tin tên, mô tả, địa điểm, thời gian)
    * Cấu hình sơ đồ chỗ ngồi (SeatMap)

#### 🟠 **3. Event được duyệt (nếu cần)**

* 📍 **Event Service**

    * Admin hoặc hệ thống duyệt sự kiện

#### 🟠 **4. Organizer tạo các loại vé**

* 📍 **Ticket Service**

    * Tạo các loại vé (loại, giá, số lượng, thời gian mở bán)
    * Hệ thống phân bổ vé theo SeatMap

---

### **II. Customer đăng ký, chọn vé và thanh toán**

#### 🔵 **5. Customer đăng ký/đăng nhập**

* 📍 **Auth Service**

    * Đăng ký hoặc đăng nhập tài khoản Customer
    * Nhận token xác thực

#### 🔵 **6. Customer duyệt danh sách sự kiện**

* 📍 **API Gateway → Event Service**

    * Lọc sự kiện theo thời gian, địa điểm, danh mục

#### 🔵 **7. Customer chọn sự kiện và loại vé**

* 📍 **API Gateway → Ticket Service**

    * Chọn loại vé và số lượng
    * Hệ thống kiểm tra vé còn lại (availability)

#### 🔵 **8. Customer tạo đơn hàng**

* 📍 **Order Service**

    * Tạo đơn hàng mới
    * Cập nhật số lượng vé đã chọn (lock temporary)

#### 🔵 **9. Customer thanh toán**

* 📍 **Order Service → Payment Service**

    * Tích hợp thanh toán qua các cổng (VNPay, Momo, Stripe…)
    * Sau khi thanh toán thành công → cập nhật trạng thái đơn hàng

#### 🔵 **10. Gửi thông báo & vé điện tử**

* 📍 **Notification Service**

    * Gửi email xác nhận đặt vé thành công
    * Gửi kèm file PDF vé & QR Code

---

### **III. Sau thanh toán**

* 📍 **Order Service**

    * Lưu thông tin lịch sử đơn hàng
    * Cập nhật báo cáo doanh thu, phân tích số lượng vé bán
* 📍 **Ticket Service**

    * Cập nhật tồn kho vé (inventory)

---

## ✅ **Tóm tắt theo thứ tự service**

| Bước | Service liên quan           | Mô tả ngắn                   |
| ---- | --------------------------- | ---------------------------- |
| 1    | Auth Service                | Organizer đăng ký tài khoản  |
| 2    | Auth Service, Event Service | Tạo sự kiện & sơ đồ chỗ ngồi |
| 3    | Event Service               | Duyệt sự kiện (nếu cần)      |
| 4    | Ticket Service              | Tạo loại vé và phân bổ vé    |
| 5    | Auth Service                | Customer đăng nhập           |
| 6    | Event Service               | Xem danh sách sự kiện        |
| 7    | Ticket Service              | Chọn loại vé và kiểm tra tồn |
| 8    | Order Service               | Tạo đơn hàng                 |
| 9    | Payment Service             | Thanh toán đơn hàng          |
| 10   | Notification Service        | Gửi vé PDF và QR code        |

---