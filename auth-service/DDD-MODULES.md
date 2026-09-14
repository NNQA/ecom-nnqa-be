# Các module DDD trong Auth Service

`auth-domain` chứa nghiệp vụ cốt lõi của xác thực. Các module khác (application, infrastructure, interface) sẽ phối hợp với domain nhưng không đặt luật nghiệp vụ vào đó.

## Domain

- **model**: Entity, Aggregate và Value Object (ví dụ User, Email, UserId). Đây là mô hình phản ánh trực tiếp nghiệp vụ.
- **repository**: Các interface mô tả nhu cầu lưu/đọc Aggregate. Domain chỉ biết *cần gì*, không biết database nào thực hiện.
- **service**: Domain Service cho quy tắc nghiệp vụ không thuộc riêng một Entity nào.
- **event**: Domain Event mô tả sự kiện nghiệp vụ đã xảy ra, như UserRegistered.
- **exception**: Các lỗi do vi phạm quy tắc nghiệp vụ, giúp lỗi được biểu đạt rõ ràng.

## Application

Điều phối các use case (đăng ký, đăng nhập, đổi mật khẩu), gọi domain và các port bên ngoài. Module này không thay thế luật nghiệp vụ của domain.

## Infrastructure

Chứa phần triển khai kỹ thuật như JPA/SQL, JWT, RabbitMQ/Kafka và cấu hình. Đây là nơi kết nối các interface của domain/application với công nghệ cụ thể.

## Interfaces (Adapter)

Nhận và trả dữ liệu qua REST, messaging hoặc giao diện khác. Adapter chuyển dữ liệu bên ngoài thành lệnh cho application.

### Nguyên tắc phụ thuộc

`interfaces → application → domain`  
`infrastructure` triển khai các interface mà application/domain yêu cầu; domain không phụ thuộc framework hay hạ tầng.
