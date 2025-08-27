import React, { useEffect, useState, useContext, useMemo } from "react";
import { useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";
import { Container, Row, Col, Card, Form, Button, Alert } from "react-bootstrap";
import cookie from "react-cookies";

const ChiTietBaiDauGia = () => {
  const { id } = useParams();
  const user = useContext(MyUserContext);

  const [phien, setPhien] = useState(null);
  const [giaDauGia, setGiaDauGia] = useState("");
  const [bids, setBids] = useState([]);
  const [message, setMessage] = useState({ type: "", text: "" });
  const [inputError, setInputError] = useState("");

  // tải chi tiết phiên
  useEffect(() => {
    const fetchPhien = async () => {
      try {
        const res = await authApis().get(`${endpoints["cuoc-dau-gia"]}/${id}`);
        setPhien(res.data);
      } catch (err) {
        console.error("Lỗi khi tải chi tiết phiên:", err);
      }
    };
    fetchPhien();

    const fetchBids = async () => {
      try {
        const res = await authApis().get(`${endpoints["lich-su-dat-gia"]}${id}`);
        setBids(res.data || []);
      } catch (e) {
        console.error("Lỗi tải lịch sử đặt giá:", e);
      }
    };
    fetchBids();
  }, [id]);

  const { sp, endTime, ended, isOwner, currentHighestBid, minBid } = useMemo(() => {
    if (!phien) {
      return {
        sp: null,
        endTime: null,
        ended: false,
        isOwner: false,
        currentHighestBid: 0,
        minBid: 0,
      };
    }
    const _sp = phien.sanPham;
    const _end = new Date(phien.thoiGianKetThuc);
    const _ended = new Date() > _end || phien.giaChot !== null;
    const _isOwner = !!(user && _sp?.nguoiDung?.id === user.id);
    const highest = phien.giaHienTai ?? null;        // null nếu chưa có ai đặt
    const hasBid = highest !== null;

    const min = hasBid
      ? Number(highest) + Number(_sp.buocNhay)
      : Number(_sp.giaKhoiDiem);

    return {
      sp: _sp,
      endTime: _end,
      ended: _ended,
      isOwner: _isOwner,
      currentHighestBid: hasBid ? Number(highest) : 0,
      minBid: min,
    };
  }, [phien, user]);

  const winnerName = phien?.nguoiThangDauGia?.hoTen;
  const finalPrice = phien?.giaChot;

  const handleSubmit = async (e) => {
    e.preventDefault();

    // chặn luôn ở client
    if (isOwner) {
      setMessage({ type: "warning", text: "Bạn không thể đấu giá sản phẩm của chính mình." });
      return;
    }
    if (ended) {
      setMessage({ type: "warning", text: "Phiên đấu giá đã kết thúc." });
      return;
    }

    const gia = parseInt(giaDauGia, 10);

    if (isNaN(gia) || gia < minBid || gia % sp.buocNhay !== 0) {
      if (isNaN(gia)) {
        setInputError("Vui lòng nhập số hợp lệ.");
      } else if (gia < minBid) {
        setInputError(
          `Giá hiện tại là ${currentHighestBid.toLocaleString()} đ. Vui lòng nhập giá ≥ ${minBid.toLocaleString()} đ`
        );
      } else {
        setInputError(`Giá bạn nhập phải chia hết cho bước nhảy (${sp.buocNhay.toLocaleString()})`);
      }
      return;
    }
    setInputError("");

    try {
      await authApis().post(
        endpoints["dat-gia"],
        { phienDauGiaId: id, gia: giaDauGia },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${cookie.load("token")}`,
          },
        }
      );

      setMessage({ type: "success", text: "Đấu giá thành công!" });
      setGiaDauGia("");

      // reload phiên
      const res1 = await authApis().get(`${endpoints["cuoc-dau-gia"]}/${id}`);
      setPhien(res1.data);
      //reload lịch sử
      const res2 = await authApis().get(`${endpoints["lich-su-dat-gia"]}${id}`);
      setBids(res2.data);
    } catch (err) {
      console.error(err);
      const serverMsg = err?.response?.data || "Lỗi khi đặt giá!";
      setMessage({ type: "danger", text: serverMsg });
    }
  };

  if (!phien) return <p className="text-center mt-5">Đang tải...</p>;

  const showForm = !ended && !isOwner;

  return (
    <Container className="mt-4">
      {phien.nguoiDang && (
        <div className="d-flex align-items-center mb-3">
          <img
            src={phien.nguoiDang.avatar || "https://via.placeholder.com/40"}
            alt="avatar"
            width={40}
            height={40}
            className="rounded-circle me-2"
          />
          <span className="small text-muted">{phien.nguoiDang.hoTen}</span>
        </div>
      )}
      <Row>
        <Col md={7}>
          <Card body>
            <h4 style={{ color: "#654be9ff", fontWeight: "bold" }}>
              {sp.tenSanPham}
            </h4>
            <p>
              <strong>Loại sản phẩm:</strong> {sp.loaiSanPham?.tenLoai}
            </p>
            <p>
              <strong>Mô tả:</strong> {sp.moTa}
            </p>
            <p>
              <strong>Giá khởi điểm:</strong> {sp.giaKhoiDiem.toLocaleString()} đ
            </p>
            <p>
              <strong>Bước nhảy:</strong> {sp.buocNhay.toLocaleString()} đ
            </p>
            <p>
              <strong>Thời gian kết thúc:</strong> {endTime.toLocaleString("vi-VN")}
            </p>
            <p>
              <strong>Giá hiện tại:</strong>{" "}
              {phien.giaHienTai != null
                ? `${phien.giaHienTai.toLocaleString()} đ`
                : "Chưa có"}
            </p>

            {!showForm && (
              <>
                {isOwner && <Alert variant="warning">Bạn là người đăng sản phẩm này nên không thể đặt giá.</Alert>}
                {ended && (
                  <>
                    <Alert variant="secondary">Phiên đấu giá đã kết thúc!</Alert>
                    {winnerName && finalPrice && (
                      <Alert variant="success">
                        Người thắng cuộc: <strong>{winnerName}</strong> với giá{" "}
                        <strong>{finalPrice.toLocaleString()} đ</strong>
                      </Alert>
                    )}
                  </>
                )}
              </>
            )}

            {showForm && (
              <>
                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-2">
                    <Form.Label>Nhập giá của bạn (≥ {minBid.toLocaleString()} đ)</Form.Label>
                    <Form.Control
                      type="number"
                      value={giaDauGia}
                      onChange={(e) => setGiaDauGia(e.target.value)}
                      required
                    />
                    {inputError && <div className="text-danger mt-1">{inputError}</div>}
                  </Form.Group>
                  <Button type="submit" variant="success">
                    Đặt giá
                  </Button>
                </Form>
                {message.text && <Alert variant={message.type} className="mt-3">{message.text}</Alert>}
              </>
            )}
          </Card>

          <Card body className="mt-3">
            <h6 className="mb-3">Lịch sử đặt giá</h6>
            {bids.length === 0 ? (
              <div className="text-muted">Chưa có ai đặt giá.</div>
            ) : (
              <div className="list-group">
                {bids.map((b, i) => (
                  <div key={i} className="list-group-item d-flex align-items-center">
                    <img
                      src={b.avatar || "/default-avatar.png"}
                      alt="avatar"
                      width={36}
                      height={36}
                      style={{ borderRadius: 18, marginRight: 10 }}
                    />
                    <div style={{ flex: 1 }}>
                      <div style={{ fontWeight: 600 }}>
                        {b.hoTen} {b.caoNhat && <span className="badge bg-success ms-2">Cao nhất</span>}
                      </div>
                      <div style={{ fontSize: 12, color: "#666" }}>
                        {new Date(b.thoiGian).toLocaleString("vi-VN")}
                      </div>
                    </div>
                    <div style={{ fontWeight: 700 }}>{Number(b.giaDau).toLocaleString()} đ</div>
                  </div>
                ))}
              </div>
            )}
          </Card>
        </Col>

        <Col md={5}>
          <Card>
            <Card.Img variant="top" src={sp.hinhAnh || "https://via.placeholder.com/400"} />
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ChiTietBaiDauGia;
