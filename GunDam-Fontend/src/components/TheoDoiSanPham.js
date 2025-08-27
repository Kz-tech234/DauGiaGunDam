import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Spinner, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const TheoDoiSanPham = () => {
    const [dsTheoDoi, setDsTheoDoi] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchTheoDoi = async () => {
        try {
            const res = await authApis().get(endpoints["theo-doi"]);
            setDsTheoDoi(res.data);
        } catch (err) {
            console.error("Lỗi khi lấy danh sách theo dõi:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTheoDoi();
    }, []);

    const handleBoTheoDoi = async (phienId) => {
        try {
            await authApis().delete(endpoints["bo-theo-doi"], {
                params: { phienId }
            });
            setDsTheoDoi(prev => prev.filter(item => item.phien.id !== phienId));
        } catch (err) {
            console.error("Lỗi khi bỏ theo dõi:", err);
        }
    };

    if (loading) return <div className="text-center mt-5"><Spinner animation="border" /></div>;

    return (
        <Container className="mt-4">
            <h2 className="text-center mb-4">Danh sách đấu giá đang theo dõi</h2>

            {dsTheoDoi.length === 0 ? (
                <p className="text-center">Bạn chưa theo dõi phiên đấu giá nào.</p>
            ) : (
                <Row>
                    {dsTheoDoi.map((item) => {
                        const phien = item.phien;
                        return (
                            <Col key={phien.id} xs={12} className="mb-4">
                                <Card className="p-3 shadow-sm">
                                    <Row>
                                        <Col md={3} xs={12} className="d-flex align-items-center justify-content-center">
                                            <Card.Img
                                                src={phien.sanPham?.hinhAnh || "https://via.placeholder.com/300x200"}
                                                alt="Hình sản phẩm"
                                                style={{ width: "100%", maxHeight: "180px", objectFit: "cover", borderRadius: "8px" }}
                                            />
                                        </Col>
                                        <Col md={9} xs={12}>
                                            <h5 className="fw-bold">{phien.sanPham?.tenSanPham}</h5>
                                            <p><strong>Mô tả:</strong> {phien.sanPham?.moTa}</p>
                                            <p><strong>Giá khởi điểm:</strong> {phien.sanPham?.giaKhoiDiem?.toLocaleString()} đ</p>
                                            <p><strong>Kết thúc:</strong> {new Date(phien.thoiGianKetThuc).toLocaleString("vi-VN")}</p>
                                            <Link to={`/cuoc-dau-gia/${phien.id}`}>
                                                <Button variant="primary" size="sm">Xem chi tiết</Button>
                                            </Link>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="ms-2"
                                                onClick={() => handleBoTheoDoi(phien.id)}
                                            >
                                                Bỏ theo dõi
                                            </Button>
                                        </Col>
                                    </Row>
                                </Card>
                            </Col>
                        );
                    })}
                </Row>
            )}
        </Container>
    );
};

export default TheoDoiSanPham;
