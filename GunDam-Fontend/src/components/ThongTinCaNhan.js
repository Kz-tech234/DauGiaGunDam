import React, { useContext, useState, useEffect } from "react";
import { Container, Row, Col, Card, Image, Form, Button, Alert } from "react-bootstrap";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";

const ThongTinCaNhan = () => {
    const user = useContext(MyUserContext);
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState({ type: "", text: "" });
    const [roleUpdated, setRoleUpdated] = useState(false);
    const [taiKhoan, setTaiKhoan] = useState(null);

    useEffect(() => {
        const fetchTaiKhoan = async () => {
            try {
                const res = await authApis().get(endpoints["tk-nganhang-cua-toi"]);
                if (Array.isArray(res.data) && res.data.length > 0) {
                    setTaiKhoan(res.data[0]);  // ✅ lấy phần tử đầu tiên
                    console.log("Dữ liệu tài khoản ngân hàng:", res.data[0]);
                }
            } catch (err) {
                console.error("Lỗi khi lấy tài khoản:", err);
            }
        };

        fetchTaiKhoan();
    }, []);

    const handleChangePassword = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setMessage({ type: "danger", text: "Mật khẩu mới không khớp!" });
            return;
        }

        try {
            const res = await authApis().post(endpoints["change-password"], {
                oldPassword: currentPassword,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            });

            if (res.status === 200) {
                setMessage({ type: "success", text: "Đổi mật khẩu thành công!" });
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            }
        } catch (err) {
            const msg = err?.response?.data || "Lỗi khi đổi mật khẩu!";
            setMessage({ type: "danger", text: msg });
        }
    };

    const handleUpgradeToSeller = async () => {
        try {
            const res = await authApis().post(endpoints["chuyen-vai-tro"]);
            if (res.status === 200) {
                setRoleUpdated(true);
                setMessage({ type: "success", text: "Chuyển vai trò thành công, vui lòng đăng nhập lại." });
                cookie.remove("token");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            }
        } catch (err) {
            const msg = err?.response?.data || "Lỗi khi chuyển vai trò!";
            setMessage({ type: "danger", text: msg });
        }
    };

    if (!user) return <Container><Alert variant="danger">Vui lòng đăng nhập để xem thông tin cá nhân.</Alert></Container>;

    return (
        <Container className="py-4">
            <Row>
                <Col md={4}>
                    <Card className="text-center p-3">
                        <Image
                            src={user.avatar || "https://via.placeholder.com/150"}
                            roundedCircle
                            width={120}
                            height={120}
                            className="mx-auto mb-3"
                        />
                        <h5>{user.hoTen || user.username}</h5>
                        <p className="text-muted mb-1">{user.email}</p>
                        <p className="mb-1"><strong>SĐT:</strong> {user.soDienThoai || "Chưa cập nhật"}</p>
                        <p className="mb-1"><strong>Địa chỉ:</strong> {user.diaChi || "Chưa cập nhật"}</p>
                        <p className="mb-2"><strong>Vai trò:</strong> {user.vaiTro}</p>

                        {/* Nút chuyển vai trò nếu đang là người mua */}
                        {user.vaiTro === "ROLE_NGUOIMUA" && !roleUpdated && (
                            <Button variant="warning" onClick={handleUpgradeToSeller}>
                                Chuyển vai trò sang người bán
                            </Button>
                        )}

                        {taiKhoan && (
                            <>
                                <hr />
                                <p className="mb-1"><strong>Tên người nhận:</strong> {taiKhoan.tenNguoiNhan}</p>
                                <p className="mb-1"><strong>Ngân hàng:</strong> {taiKhoan.nganHang}</p>
                                <p className="mb-1"><strong>Số tài khoản:</strong> {taiKhoan.soTaiKhoan}</p>
                            </>
                        )}
                    </Card>
                </Col>

                <Col md={8}>
                    <Card body>
                        <h4 className="mb-4">Đổi mật khẩu</h4>
                        {message.text && (
                            <Alert variant={message.type}>{message.text}</Alert>
                        )}
                        <Form onSubmit={handleChangePassword}>
                            <Form.Group className="mb-3">
                                <Form.Label>Mật khẩu hiện tại</Form.Label>
                                <Form.Control
                                    type="password"
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Mật khẩu mới</Form.Label>
                                <Form.Control
                                    type="password"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Nhập lại mật khẩu mới</Form.Label>
                                <Form.Control
                                    type="password"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    required
                                />
                            </Form.Group>

                            <Button type="submit" variant="primary">Xác nhận đổi mật khẩu</Button>
                        </Form>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default ThongTinCaNhan;
