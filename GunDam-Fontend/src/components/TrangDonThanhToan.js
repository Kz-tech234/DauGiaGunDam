// src/pages/DonThanhToan.js
import React, { useEffect, useState } from "react";
import { Container, Card, Row, Col, Form, Button, Badge, Alert } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";

const TrangDonThanhToan = () => {
  const [orders, setOrders] = useState([]);
  const [taiKhoanMap, setTaiKhoanMap] = useState({});
  const [message, setMessage] = useState(null);
  const [submittingId, setSubmittingId] = useState(null);

  const STATUS_LABELS = {
    PENDING: "Chưa trả",
    SELLER_REVIEW: "Đang chờ người bán duyệt",
    PAID: "Đã trả",
    CANCELLED: "Đã hủy"
  };

  const STATUS_BADGE = { 
    PENDING: "warning", //vàng
    SELLER_REVIEW: "primary", //xanh dương
    PAID: "success", //xanh lá
    CANCELLED: "secondary" // màu xám
   };

  const fetchOrders = async () => {
    try {
      const res = await authApis().get(endpoints["don-cua-toi"]);
      // thêm field tạm để bind input nếu chưa có
      const data = (res.data || []).map(d => ({
        ...d,
        _hoTenNhan: d.hoTenNhan || "",
        _soDienThoai: d.soDienThoai || "",
        _diaChiNhan: d.diaChiNhan || "",
        _phuongThuc: d.phuongThuc || "COD",
        _ghiChu: d.ghiChu || "",
        _minhChungFile: null
      }));
      setOrders(data);
    } catch (err) {
      console.error("Lỗi tải đơn:", err);
      setMessage({ type: "danger", text: "Không tải được hóa đơn của bạn." });
    }
  };

  const fetchTaiKhoanNguoiBan = async (phienId) => {
    try {
      const res = await authApis().get(endpoints["tk-nguoi-ban"](phienId));
      setTaiKhoanMap(prev => ({ ...prev, [phienId]: res.data }));
    } catch (err) {
      console.error("Lỗi lấy tài khoản người bán:", err);
    }
  };

  useEffect(() => { fetchOrders(); }, []);

  const setField = (id, field, value, don) => {
    setOrders(prev => prev.map(d => (d.id === id ? { ...d, [field]: value } : d)));

    if (field === "_phuongThuc" && value === "BANK") {
      const phienId = don?.phienDauGia?.id;
      if (phienId && !taiKhoanMap[phienId]) {
        fetchTaiKhoanNguoiBan(phienId);
      }
    }
  };

  const onChangeMinhChung = (donId, file) => {
    const preview = file ? URL.createObjectURL(file) : null;
    setOrders(prev => prev.map(d => d.id === donId ? { ...d, _minhChungFile: file, _minhChungPreview: preview } : d));
  };

  const validate = (d) => {
    if (!d._hoTenNhan?.trim()) return "Vui lòng nhập Họ tên nhận.";
    if (!d._soDienThoai?.trim()) return "Vui lòng nhập Số điện thoại.";
    if (!/^\d{8,15}$/.test(d._soDienThoai.trim())) return "Số điện thoại không hợp lệ.";
    if (!d._diaChiNhan?.trim()) return "Vui lòng nhập Địa chỉ nhận.";
    return "";
  };

  const handlePay = async (don) => {
    setMessage(null);

    const token = cookie.load("token");
    if (!token) {
      setMessage({ type: "danger", text: "Bạn cần đăng nhập để thanh toán." });
      return;
    }

    const v = validate(don);
    if (v) {
      setMessage({ type: "danger", text: v });
      return;
    }

    setSubmittingId(don.id);

    try {
      const payload = {
        phuongThuc: don._phuongThuc, // "COD" hoặc "BANK"
        hoTenNhan: don._hoTenNhan,
        soDienThoai: don._soDienThoai,
        diaChiNhan: don._diaChiNhan,
        ghiChu: don._ghiChu || ""
      };

      await authApis().post(
        endpoints["thanh-toan-don"](don.id),
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      setMessage({ type: "success", text: "Thanh toán thành công!" });
      await fetchOrders();
    } catch (err) {
      let msg = err?.response?.data || "Thanh toán thất bại!";
      if (typeof msg === "string" && msg.startsWith("<!doctype")) {
        msg = "Không xác thực được (401). Vui lòng đăng nhập lại.";
      }
      setMessage({ type: "danger", text: msg });
    } finally {
      setSubmittingId(null);
    }
  };


  const pending = orders.filter(o => o.trangThai === "PENDING");

  return (
    <Container className="mt-4">
      <div className="d-flex align-items-center mb-3">
        <h3 className="mb-0">Hóa đơn đã thắng</h3>
        <Badge bg="warning" text="dark" className="ms-2">{pending.length} chờ thanh toán</Badge>
      </div>

      {message && <Alert variant={message.type}>{message.text}</Alert>}

      {orders.length === 0 ? (
        <div className="text-muted">Bạn chưa có hóa đơn nào.</div>
      ) : (
        <Row xs={1} md={2} className="g-3">
          {orders.map((d) => {
            const sp = d?.phienDauGia?.sanPham;
            const status = d.trangThai;
            const statusLabel = STATUS_LABELS[status] ?? status;
            const badgeVariant = STATUS_BADGE[status] ?? "secondary";

            return (
              <Col key={d.id}>
                <Card className="h-100 shadow-sm">
                  <Card.Body>
                    <div className="d-flex justify-content-between align-items-start">
                      <Card.Title className="mb-2" style={{ maxWidth: "70%" }}>
                        {sp?.tenSanPham || `Phiên #${d?.phienDauGia?.id}`}
                      </Card.Title>
                      <Badge bg={badgeVariant}>{statusLabel}</Badge>
                    </div>

                    <div className="mb-2">
                      <strong>Giá phải trả:</strong> {Number(d.soTien || 0).toLocaleString("vi-VN")} đ
                    </div>
                    <div className="text-muted mb-3" style={{ fontSize: 13 }}>
                      Tạo lúc {new Date(d.ngayTao).toLocaleString("vi-VN")}
                    </div>

                    {status === "PENDING" ? (
                      <>
                        <Form>
                          <Row className="g-2">
                            <Col md={6}>
                              <Form.Group>
                                <Form.Label>Họ tên nhận</Form.Label>
                                <Form.Control
                                  value={d._hoTenNhan}
                                  onChange={(e) => setField(d.id, "_hoTenNhan", e.target.value)}
                                />
                              </Form.Group>
                            </Col>
                            <Col md={6}>
                              <Form.Group>
                                <Form.Label>Số điện thoại</Form.Label>
                                <Form.Control
                                  value={d._soDienThoai}
                                  onChange={(e) => setField(d.id, "_soDienThoai", e.target.value)}
                                />
                              </Form.Group>
                            </Col>
                          </Row>

                          <Form.Group className="mt-2">
                            <Form.Label>Địa chỉ nhận</Form.Label>
                            <Form.Control
                              value={d._diaChiNhan}
                              onChange={(e) => setField(d.id, "_diaChiNhan", e.target.value)}
                            />
                          </Form.Group>

                          <Row className="g-2 mt-2">
                            <Col md={6}>
                              <Form.Group>
                                <Form.Label>Phương thức</Form.Label>
                                <Form.Select
                                  value={d._phuongThuc}
                                  onChange={(e) => setField(d.id, "_phuongThuc", e.target.value, d)}
                                >
                                  <option value="COD">COD (Thanh toán khi nhận)</option>
                                  <option value="BANK">Chuyển khoản</option>
                                </Form.Select>
                              </Form.Group>
                            </Col>
                            <Col md={6}>
                              <Form.Group>
                                <Form.Label>Ghi chú</Form.Label>
                                <Form.Control
                                  value={d._ghiChu}
                                  onChange={(e) => setField(d.id, "_ghiChu", e.target.value)}
                                />
                              </Form.Group>
                            </Col>
                          </Row>
                          {d._phuongThuc === "BANK" && (
                            <div className="mt-3">
                              {taiKhoanMap[d?.phienDauGia?.id] && (
                                <div className="border rounded p-3 bg-light mb-3">
                                  <h6 className="mb-2">💳 Thông tin chuyển khoản</h6>
                                  <div><strong>Ngân hàng:</strong> {taiKhoanMap[d.phienDauGia.id].nganHang}</div>
                                  <div><strong>Số tài khoản:</strong> {taiKhoanMap[d.phienDauGia.id].soTaiKhoan}</div>
                                  <div><strong>Chủ tài khoản:</strong> {taiKhoanMap[d.phienDauGia.id].tenNguoiNhan}</div>
                                  {taiKhoanMap[d.phienDauGia.id].qrUrl && (
                                    <div className="mt-2">
                                      <img src={taiKhoanMap[d.phienDauGia.id].qrUrl} alt="QR chuyển khoản" style={{ maxWidth: 200 }} />
                                    </div>
                                  )}
                                </div>
                              )}
                            </div>
                          )}
                        </Form>


                        <div className="d-flex justify-content-end mt-3">
                          <Button
                            variant="success"
                            disabled={submittingId === d.id}
                            onClick={() => handlePay(d)}
                          >
                            {submittingId === d.id ? "Đang xử lý..." : "Thanh toán"}
                          </Button>
                        </div>
                      </>
                    ) : (
                      <>
                        <div><strong>Trạng thái:</strong> {statusLabel}</div>
                        <div><strong>Phương thức:</strong> {d.phuongThuc}</div>
                        {d.ngayThanhToan && (
                          <div><strong>Ngày thanh toán:</strong> {new Date(d.ngayThanhToan).toLocaleString("vi-VN")}</div>
                        )}
                      </>
                    )}
                  </Card.Body>
                </Card>
              </Col>
            );
          })}
        </Row>
      )}
    </Container>
  );
};

export default TrangDonThanhToan;
