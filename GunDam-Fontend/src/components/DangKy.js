import { useState } from 'react';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import '../css/DangKy.css';

const DangKy = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    hoTen: '',
    email: '',
    soDienThoai: '',
    diaChi: '',
    vaiTro: 'ROLE_NGUOIMUA',
    avatar: '',
  });
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleFileChange = (e) => {
    setFormData({
      ...formData,
      avatar: e.target.files[0],
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      setErrorMessage('Mật khẩu không khớp');
      return;
    }

    const data = new FormData();
    Object.keys(formData).forEach((key) => {
      if (key !== 'confirmPassword') {
        data.append(key, formData[key]);
      }
    });

    try {
      const res = await authApis().post(endpoints['add-user'], data, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setSuccessMessage('Đăng ký thành công, vui lòng đợi admin duyệt');
      setTimeout(() => navigate('/dangnhap'), 2000); // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
    } catch (err) {
      setErrorMessage('Đăng ký thất bại, vui lòng thử lại');
    }
  };

  // Hàm tự động điều chỉnh chiều cao của textarea khi người dùng nhập
  const handleAutoResize = (e) => {
    e.target.style.height = 'auto';  // Đặt chiều cao về 'auto' để đo chiều cao mới
    e.target.style.height = `${e.target.scrollHeight}px`;  // Cập nhật chiều cao theo chiều dài nội dung
  };


  return (
    <Container className="signup-container">
      <Row className="justify-content-center">
        <Col md={6}>
          <h2 className="text-center mb-4">Đăng ký tài khoản</h2>
          {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
          {successMessage && <Alert variant="success">{successMessage}</Alert>}
          <Form onSubmit={handleSubmit} className="form-style">
            <Form.Group controlId="username" className="mb-3">
              <Form.Label>Tài khoản</Form.Label>
              <Form.Control
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="password" className="mb-3">
              <Form.Label>Mật khẩu</Form.Label>
              <Form.Control
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="confirmPassword" className="mb-3">
              <Form.Label>Xác nhận mật khẩu</Form.Label>
              <Form.Control
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="hoTen" className="mb-3">
              <Form.Label>Họ tên</Form.Label>
              <Form.Control
                type="text"
                name="hoTen"
                value={formData.hoTen}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="email" className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="soDienThoai" className="mb-3">
              <Form.Label>Số điện thoại</Form.Label>
              <Form.Control
                type="text"
                name="soDienThoai"
                value={formData.soDienThoai}
                onChange={handleChange}
                required
                className="form-input"
              />
            </Form.Group>

            <Form.Group controlId="diaChi" className="mb-3">
              <Form.Label>Địa chỉ</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="diaChi"
                value={formData.diaChi}
                onChange={handleChange}
                onInput={handleAutoResize}  /* Tự động điều chỉnh chiều cao khi nhập */
                required
                className="form-input"
              />
            </Form.Group>


            <Form.Group controlId="vaiTro" className="mb-3">
              <Form.Label>Vai trò</Form.Label>
              <div>
                <Form.Check
                  inline
                  label="Người mua"
                  name="vaiTro"
                  type="radio"
                  value="ROLE_NGUOIMUA"
                  checked={formData.vaiTro === "ROLE_NGUOIMUA"}
                  onChange={handleChange}
                />
                <Form.Check
                  inline
                  label="Người bán"
                  name="vaiTro"
                  type="radio"
                  value="ROLE_NGUOIBAN"
                  checked={formData.vaiTro === "ROLE_NGUOIBAN"}
                  onChange={handleChange}
                />
              </div>
            </Form.Group>

            <Form.Group controlId="avatar" className="mb-3">
              <Form.Label>Ảnh đại diện</Form.Label>
              <Form.Control
                type="file"
                name="avatar"
                onChange={handleFileChange}
                className="form-input"
              />
            </Form.Group>

            <Button variant="primary" type="submit" className="submit-button" block>
              Đăng ký
            </Button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default DangKy;
