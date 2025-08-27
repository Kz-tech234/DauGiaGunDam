import React, { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";
import { MyUserContext } from "../configs/Contexts";
import { Container, Row, Col, Card, Button, Carousel } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import '../css/TrangChu.css';

const TrangChu = () => {
    const user = useContext(MyUserContext);
    const [auctions, setAuctions] = useState([]);

    useEffect(() => {
        const fetchAuctions = async () => {
            try {
                const res = await authApis().get(endpoints["cuoc-dau-gia"]); // Lấy danh sách các phiên đấu giá
                setAuctions(res.data);
            } catch (error) {
                console.error("Lỗi khi tải danh sách các cuộc đấu giá:", error);
            }
        };
        fetchAuctions();
    }, []);

    return (
        <Container className="mt-5">
            {/* Phần giới thiệu */}
            <Row className="justify-content-center mb-5">
                <Col md={8} className="text-center">
                    <h1 className="fw-bold mb-3 text-uppercase text-gradient">
                        Chào mừng đến với hệ thống đấu giá trực tuyến
                    </h1>
                    <p className="fs-5 text-muted">
                        Khám phá những phiên đấu giá độc đáo và sưu tầm những món đồ quý giá!
                    </p>
                    {!user ? (
                        <div className="d-flex justify-content-center gap-3 mt-4">
                            <Link to="/dangnhap">
                                <Button variant="outline-light" size="lg" className="btn-hover btn-hover-light">
                                    Đăng nhập
                                </Button>
                            </Link>
                            <Link to="/dangky">
                                <Button variant="primary" size="lg" className="btn-hover">
                                    Đăng ký
                                </Button>
                            </Link>
                        </div>
                    ) : (
                        <p className="fs-5 text-success mt-4">Chúc bạn một phiên đấu giá thành công!</p>
                    )}
                </Col>
            </Row>

            {/* Tính năng chính */}
            <h3 className="text-center text-secondary mb-5">Tính năng chính</h3>
            <Row className="text-center">
                <Col md={4}>
                    <Card className="mb-4 shadow-lg effect-card">
                        <Card.Body>
                            <Link to="/cuocdaugia" className="text-decoration-none text-dark">
                                <Card.Title>🔨 Tham gia đấu giá</Card.Title>
                                <Card.Text>
                                    Theo dõi và tham gia các phiên đấu giá trực tiếp với nhiều sản phẩm đặc sắc.
                                </Card.Text>
                            </Link>
                        </Card.Body>
                    </Card>
                </Col>
                {user?.vaiTro === "ROLE_NGUOIBAN" && (
                    <Col md={4}>
                        <Card className="mb-4 shadow-lg effect-card">
                            <Card.Body>
                                <Link to="/taodaugia" className="text-decoration-none text-dark">
                                    <Card.Title>📦 Tạo đấu giá</Card.Title>
                                    <Card.Text>Đăng sản phẩm để đấu giá và quản lý trạng thái giao dịch.</Card.Text>
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                )}
                <Col md={4}>
                    <Card className="mb-4 shadow-lg effect-card">
                        <Card.Body>
                            <Link to="/thongtincanhan" className="text-decoration-none text-dark">
                                <Card.Title>👤 Hồ sơ cá nhân</Card.Title>
                                <Card.Text>Quản lý thông tin tài khoản, xem lịch sử giao dịch và cập nhật ảnh đại diện.</Card.Text>
                            </Link>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Slider hình ảnh */}
            <Carousel>
                <Carousel.Item>
                    <img className="d-block w-100" src="/anh.jpg" alt="First slide" />
                    <Carousel.Caption>
                        <h3 className="text-light">Khám phá các phiên đấu giá độc đáo!</h3>
                        <p className="text-light">Những món đồ tuyệt vời đang chờ đón bạn!</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src="/anh2.jpg" alt="Second slide" />
                    <Carousel.Caption>
                        <h3 className="text-light">Mua bán các sản phẩm sưu tầm</h3>
                        <p className="text-light">Đăng ký tham gia và trở thành chủ nhân của những món đồ quý giá!</p>
                    </Carousel.Caption>
                </Carousel.Item>
            </Carousel>

            <Carousel>
                <Carousel.Item>
                    <img className="d-block w-100" src="/anh.jpg" alt="Giá khởi điểm" />
                    <Carousel.Caption>
                        <h3>Giá khởi điểm</h3>
                        <p>Đặt giá thấp nhất theo quy định và tăng giá dần.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src="/anh2.jpg" alt="Không chỉnh sửa giá" />
                    <Carousel.Caption>
                        <h3>Không chỉnh sửa giá</h3>
                        <p>Giá đã đặt sẽ không thể thay đổi.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img className="d-block w-100" src="/anh.jpg" alt="Kết thúc đấu giá" />
                    <Carousel.Caption>
                        <h3>Kết thúc đấu giá</h3>
                        <p>Phiên đấu giá sẽ kết thúc khi đạt mức giá tối đa hoặc hết thời gian.</p>
                    </Carousel.Caption>
                </Carousel.Item>
            </Carousel>

            {/* Hiển thị các cuộc đấu giá */}
            <h3 className="text-center text-secondary mb-5">Các cuộc đấu giá đang diễn ra</h3>
            <Row className="text-center">
                {auctions.length > 0 ? (
                    auctions.slice(0, 5).map((auction, index) => {
                        const isEnded = new Date(auction.thoiGianKetThuc) < new Date() || auction.giaChot !== null; // Kiểm tra nếu phiên đã kết thúc
                        if (isEnded) return null; // Nếu phiên đã kết thúc thì không hiển thị
                        return (
                            <Col md={4} key={index}>
                                <Card className="mb-4 shadow-lg effect-card">
                                    <Card.Img
                                        variant="top"
                                        src={auction.sanPham.hinhAnh || "/default-product.png"}
                                        style={{ objectFit: 'cover', height: '200px' }} // Điều chỉnh ảnh để không chiếm quá nhiều diện tích
                                    />
                                    <Card.Body>
                                        <Card.Title>{auction.sanPham.tenSanPham}</Card.Title>
                                        <Card.Text>
                                            {auction.sanPham.moTa.length > 50
                                                ? `${auction.sanPham.moTa.slice(0, 50)}...`
                                                : auction.sanPham.moTa}
                                        </Card.Text>
                                        <p><strong>Giá khởi điểm:</strong> {auction.sanPham.giaKhoiDiem.toLocaleString()} đ</p>
                                        <p><strong>Thời gian bắt đầu:</strong> {new Date(auction.thoiGianBatDau).toLocaleString("vi-VN")}</p>
                                        <Link to={`/cuoc-dau-gia/${auction.id}`} className="btn btn-primary">
                                            Xem chi tiết
                                        </Link>
                                    </Card.Body>
                                </Card>
                            </Col>
                        );
                    })
                ) : (
                    <Col>
                        <p className="text-center">Hiện tại không có phiên đấu giá nào.</p>
                    </Col>
                )}
            </Row>
        </Container>
    );
};

export default TrangChu;
