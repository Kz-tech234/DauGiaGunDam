import React, { useEffect, useMemo, useState } from "react";
import { Card, Row, Col, Container, Spinner, Button, ListGroup, Badge } from "react-bootstrap";
import { Link } from "react-router-dom";
import { endpoints, authApis } from "../configs/Apis";
import "../css/TrangDauGia.css";

const TrangDauGia = () => {
  const [dsPhien, setDsPhien] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState("homNay");
  const [dsDangTheoDoi, setDsDangTheoDoi] = useState([]);
  const [tuKhoa, setTuKhoa] = useState("");
  const [nowTick, setNowTick] = useState(Date.now()); // để re-render countdown mỗi 1s

  useEffect(() => {
    const fetchPhien = async () => {
      try {
        const res = await authApis().get(endpoints["cuoc-dau-gia"]);

        const now = Date.now();
        const normalized = (res.data || []).map((p) => {
          const ended =
            new Date(p.thoiGianKetThuc).getTime() <= now ||
            p.trangThai === "da_ket_thuc" ||
            p.giaChot != null;
          return ended ? { ...p, trangThai: "da_ket_thuc" } : p;
        });

        setDsPhien(normalized);
      } catch (err) {
        console.error("Lỗi khi lấy danh sách phiên:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchPhien();

    const fetchDsDangTheoDoi = async () => {
      try {
        const res = await authApis().get(endpoints["danh-sach-theo-doi"]);
        const ids = res.data.map((item) => item.phienDauGia.id);
        setDsDangTheoDoi(ids);
      } catch (err) {
        console.error("Lỗi khi lấy danh sách đang theo dõi:", err);
      }
    };
    fetchDsDangTheoDoi();

    // tick mỗi 1s để countdown mượt, và mỗi 60s để chuẩn hóa trạng thái
    const tick = setInterval(() => setNowTick(Date.now()), 1000);
    const syncState = setInterval(() => {
      setDsPhien((prev) => {
        const now = Date.now();
        return prev.map((p) => {
          const ended =
            new Date(p.thoiGianKetThuc).getTime() <= now ||
            p.trangThai === "da_ket_thuc" ||
            p.giaChot != null;
          return ended ? { ...p, trangThai: "da_ket_thuc" } : p;
        });
      });
    }, 60000);

    return () => {
      clearInterval(tick);
      clearInterval(syncState);
    };
  }, []);

  const todayStart = useMemo(() => {
    const t = new Date();
    t.setHours(0, 0, 0, 0);
    return t.getTime();
  }, []);

  const laHomNay = (ngay) => {
    const d = new Date(ngay);
    d.setHours(0, 0, 0, 0);
    return d.getTime() === todayStart;
  };

  const laTruocHomNay = (ngay) => {
    const d = new Date(ngay);
    d.setHours(0, 0, 0, 0);
    return d.getTime() < todayStart;
  };

  const filterByTuKhoa = (list) =>
    list.filter((phien) =>
      phien.sanPham?.tenSanPham?.toLowerCase().includes(tuKhoa.toLowerCase())
    );

  const dsHomNay = dsPhien.filter(
    (p) => laHomNay(p.thoiGianBatDau) && p.trangThai !== "da_ket_thuc"
  );
  const dsTruocHomNay = dsPhien.filter(
    (p) => laTruocHomNay(p.thoiGianBatDau) && p.trangThai !== "da_ket_thuc"
  );
  const dsKetThuc = dsPhien.filter((p) => p.trangThai === "da_ket_thuc");
  const dsTheoDoi = dsPhien.filter((p) => dsDangTheoDoi.includes(p.id));

  const ChuyenTrangThaiTheoDoi = async (phienId) => {
    try {
      if (dsDangTheoDoi.includes(phienId)) {
        await authApis().delete(`${endpoints["bo-theo-doi"]}/${phienId}`);
        setDsDangTheoDoi((prev) => prev.filter((id) => id !== phienId));
      } else {
        await authApis().post(`${endpoints["them-theo-doi"]}/${phienId}`, {});
        setDsDangTheoDoi((prev) => [...prev, phienId]);
      }
    } catch (err) {
      console.error("Lỗi khi cập nhật theo dõi:", err);
    }
  };

  const formatTimeLeft = (endIso) => {
    const end = new Date(endIso).getTime();
    const diff = end - nowTick;
    if (diff <= 0) return "00:00:00";
    const h = Math.floor(diff / 3600000);
    const m = Math.floor((diff % 3600000) / 60000);
    const s = Math.floor((diff % 60000) / 1000);
    const pad = (n) => n.toString().padStart(2, "0");
    return `${pad(h)}:${pad(m)}:${pad(s)}`;
  };

  const renderBadge = (phien) => {
    if (phien.trangThai === "da_ket_thuc")
      return <Badge bg="secondary">Đã kết thúc</Badge>;
    return (
      <Badge bg="success" className="soft-badge">
        Còn lại: {formatTimeLeft(phien.thoiGianKetThuc)}
      </Badge>
    );
  };

  const renderFollowBtn = (phien) => {
    const dangTheoDoi = dsDangTheoDoi.includes(phien.id);
    return (
      <Button
        variant={dangTheoDoi ? "outline-danger" : "outline-success"}
        size="sm"
        className="ms-2"
        onClick={() => ChuyenTrangThaiTheoDoi(phien.id)}
      >
        {dangTheoDoi ? "Bỏ theo dõi" : "Theo dõi"}
      </Button>
    );
  };

  const renderPhienCards = (list) => (
    <Row className="g-4">
      {list.map((phien) => {
        const daKetThuc = phien.trangThai === "da_ket_thuc";
        return (
          <Col key={phien.id} xl={6} lg={12}>
            <Card className={`auction-card ${daKetThuc ? "is-ended" : ""}`}>
              <div className="thumb-wrap">
                <Card.Img
                  src={phien.sanPham?.hinhAnh || "https://via.placeholder.com/600x400"}
                  alt="Hình sản phẩm"
                  className="auction-thumb"
                />
                <div className="badge-wrap">{renderBadge(phien)}</div>
              </div>

              <Card.Body>

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
                <div className="d-flex justify-content-between align-items-start mb-2">
                  <h5 className="fw-bold mb-0 text-truncate">
                    {phien.sanPham?.tenSanPham}
                  </h5>
                  {/* Chip giá nhanh */}
                </div>

                {phien.sanPham?.moTa && (
                  <p className="text-secondary small clamp-2 mb-2">
                    {phien.sanPham.moTa}
                  </p>
                )}

                <div className="d-flex flex-wrap gap-3 mb-3">
                  <div className="kv">
                    <span className="kv-label">Giá khởi điểm</span>
                    <span className="kv-value">
                      {phien.sanPham?.giaKhoiDiem?.toLocaleString()} đ
                    </span>
                  </div>
                  <div className="kv">
                    <span className="kv-label">Kết thúc</span>
                    <span className="kv-value">
                      {new Date(phien.thoiGianKetThuc).toLocaleString("vi-VN")}
                    </span>
                  </div>
                </div>

                <div className="d-flex align-items-center">
                  <Link to={`/cuoc-dau-gia/${phien.id}`}>
                    <Button variant="primary" size="sm">
                      Xem chi tiết
                    </Button>
                  </Link>
                  {renderFollowBtn(phien)}
                </div>
              </Card.Body>
            </Card>
          </Col>
        );
      })}
    </Row>
  );

  // helper: lọc theo từ khóa + sắp xếp mới -> cũ
  const applySearchAndSort = (list, sortBy = "approve") => {
    const filtered = filterByTuKhoa(list);
    const getTs = (p) => {
      if (sortBy === "end") return new Date(p.thoiGianKetThuc).getTime();
      return new Date(p.thoiGianBatDau || p.sanPham?.ngayDang).getTime();
    };
    return [...filtered].sort((a, b) => getTs(b) - getTs(a));
  };

  const renderNoData = (label) => (
    <div className="empty-state text-center">
      <h6 className="mt-3">Không có {label}</h6>
      <p className="text-secondary small">
        Thử thay đổi từ khóa tìm kiếm hoặc chọn mục khác ở thanh bên.
      </p>
    </div>
  );

  if (loading)
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" />
      </div>
    );

  return (
    <Container className="mt-4 trang-dau-gia">
      {/* Search bar */}
      <Row className="mb-4">
        <Col md={{ span: 8, offset: 2 }}>
          <div className="search-wrap shadow-sm">
            <input
              type="text"
              placeholder="Tìm kiếm theo tên sản phẩm..."
              className="form-control search-input"
              value={tuKhoa}
              onChange={(e) => setTuKhoa(e.target.value)}
            />
          </div>
        </Col>
      </Row>

      <Row>
        {/* Sidebar */}
        <Col md={3} className="mb-3">
          <div className="sticky-side">
            <ListGroup className="rounded-3 overflow-hidden">
              <ListGroup.Item
                action
                active={tab === "homNay"}
                onClick={() => setTab("homNay")}
              >
                Bài đấu giá hôm nay
              </ListGroup.Item>
              <ListGroup.Item
                action
                active={tab === "truocDo"}
                onClick={() => setTab("truocDo")}
              >
                Hôm qua hoặc trước đó
              </ListGroup.Item>
              <ListGroup.Item
                action
                active={tab === "ketThuc"}
                onClick={() => setTab("ketThuc")}
              >
                Đã kết thúc
              </ListGroup.Item>
              <ListGroup.Item
                action
                active={tab === "dangTheoDoi"}
                onClick={() => setTab("dangTheoDoi")}
              >
                Đang theo dõi
              </ListGroup.Item>
            </ListGroup>
          </div>
        </Col>

        {/* Content */}
        <Col md={9}>
          {tab === "homNay" && (
            <>
              <h2 className="section-title">Bài đấu giá diễn ra hôm nay</h2>
              {applySearchAndSort(dsHomNay).length > 0
                ? renderPhienCards(applySearchAndSort(dsHomNay))
                : renderNoData("phiên nào hôm nay")}
            </>
          )}

          {tab === "truocDo" && (
            <>
              <h2 className="section-title">Bài đấu giá hôm qua hoặc trước đó</h2>
              {applySearchAndSort(dsTruocHomNay).length > 0
                ? renderPhienCards(applySearchAndSort(dsTruocHomNay))
                : renderNoData("phiên nào hôm qua hoặc trước đó")}
            </>
          )}

          {tab === "ketThuc" && (
            <>
              <h2 className="section-title">Bài đấu giá đã kết thúc</h2>
              {applySearchAndSort(dsKetThuc, "end").length > 0
                ? renderPhienCards(applySearchAndSort(dsKetThuc, "end"))
                : renderNoData("phiên đã kết thúc")}
            </>
          )}

          {tab === "dangTheoDoi" && (
            <>
              <h2 className="section-title">Bài đấu giá đang theo dõi</h2>
              {applySearchAndSort(dsTheoDoi).length > 0
                ? renderPhienCards(applySearchAndSort(dsTheoDoi))
                : renderNoData("phiên đang theo dõi")}
            </>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default TrangDauGia;
