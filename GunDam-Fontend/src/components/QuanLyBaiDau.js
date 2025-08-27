import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Badge } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { Link } from "react-router-dom";
import '../css/QuanLyBaiDau.css';

const QuanLyBaiDau = () => {
  const [baiDau, setBaiDau] = useState([]);

  useEffect(() => {
    const fetchBaiDau = async () => {
      try {
        const res = await authApis().get(endpoints["quan-ly-bai-dau"]);
        setBaiDau(res.data || []);
      } catch (error) {
        console.error("Lỗi:", error);
      }
    };
    fetchBaiDau();
  }, []);

  const handleXacNhan = async (donId) => {
    try {
      await authApis().put(endpoints["xac-nhan-don"](donId));
      alert("Đã xác nhận đơn thành công");
      window.location.reload();
    } catch (err) {
      console.error(" Chi tiết lỗi:", err.response?.data || err.message);
      alert("Xác nhận thất bại! " + (err.response?.data || ""));
    }
  };

  const handleHuyDon = async (donId) => {
    const lyDo = prompt("Nhập lý do hủy đơn:");
    if (!lyDo || lyDo.trim() === "") {
      alert("Vui lòng nhập lý do.");
      return;
    }

    try {
      await authApis().put(endpoints["huy-don"](donId), { lyDo });
      alert("Đã hủy đơn thành công.");
      window.location.reload();
    } catch (err) {
      alert("Hủy đơn thất bại! " + (err.response?.data || ""));
    }
  };

  return (
    <Container className="mt-4">
      <h3>Quản lý bài đấu</h3>
      <Row className="g-4">
        {baiDau.map((phien) => (
          <Col md={4} key={phien.id}>
            <Card className="custom-card position-relative">
              {/* Badge trạng thái */}
              {phien.nguoiThangDauGia && (
                <Badge
                  bg={
                    phien.donThanhToan?.trangThai === "PAID"
                      ? "success"
                      : phien.donThanhToan?.trangThai === "SELLER_REVIEW"
                        ? "info"
                        : phien.donThanhToan?.trangThai === "CANCELLED"
                          ? "secondary"
                          : "warning"
                  }
                  className="position-absolute top-0 end-0 m-2"
                >
                  {
                    phien.donThanhToan?.trangThai === "PAID"
                      ? "Đã thanh toán"
                      : phien.donThanhToan?.trangThai === "SELLER_REVIEW"
                        ? "Chờ xác nhận"
                        : phien.donThanhToan?.trangThai === "CANCELLED"
                          ? "Đã hủy"
                          : "Chưa thanh toán"
                  }
                </Badge>
              )}

              <Card.Body>
                <Row>
                  <Col md={4}>
                    <Card.Img
                      variant="top"
                      src={phien.sanPham?.hinhAnh || "https://via.placeholder.com/150"}
                      alt="Hình sản phẩm"
                      className="product-img"
                    />
                  </Col>
                  <Col md={8}>
                    <Card.Title>{phien.sanPham.tenSanPham}</Card.Title>
                    <Card.Text>
                      <strong>Giá hiện tại: </strong>
                      {phien.giaHienTai?.toLocaleString() + " đ" || "Chưa có"}
                    </Card.Text>
                    {phien.nguoiThangDauGia && (
                      <Card.Text>
                        <strong>Người thắng: </strong>
                        {phien.nguoiThangDauGia.hoTen}
                      </Card.Text>
                    )}

                    {phien.donThanhToan?.phuongThuc && (
                      <Card.Text>
                        <strong>Phương thức thanh toán: </strong>
                        {phien.donThanhToan.phuongThuc === "COD" ? "Thanh toán khi nhận hàng (COD)" : "Chuyển khoản ngân hàng (BANK)"}
                      </Card.Text>
                    )}

                    {/* Hiện nút xác nhận nếu đơn ở trạng thái SELLER_REVIEW */}
                    {phien.donThanhToan?.trangThai === "SELLER_REVIEW" && (
                      <>
                        <Button variant="danger" className="mb-2 w-100" onClick={() => handleHuyDon(phien.donThanhToan.id)}>
                          ❌ Hủy xác nhận
                        </Button>
                        <Button
                          variant="success"
                          className="mb-2 w-100"
                          onClick={() => handleXacNhan(phien.donThanhToan.id)}
                        >
                          ✅ Xác nhận đơn
                        </Button>
                      </>
                    )}

                    <Link to={`/cuoc-dau-gia/${phien.id}`}>
                      <Button variant="primary" className="w-100">Xem chi tiết</Button>
                    </Link>
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default QuanLyBaiDau;
