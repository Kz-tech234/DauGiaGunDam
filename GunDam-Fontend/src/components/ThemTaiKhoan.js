import { useState, useEffect, useRef } from "react";
import {
  Container, Row, Col, Form, Button, Spinner, Card, Toast, ToastContainer,
  InputGroup, Image, Modal, ProgressBar, Alert
} from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { useNavigate } from "react-router-dom";

const NGAN_HANGS = [
  "VCB - Vietcombank",
  "TCB - Techcombank",
  "ACB - ACB",
  "BIDV - BIDV",
  "MB - MB Bank",
  "VPB - VPBank",
  "VIB - VIB",
  "SHB - SHB",
  "TPB - TPBank",
  "HDB - HDBank",
];

const MAX_FILE_SIZE = 5 * 1024 * 1024;
const ACCEPT_TYPES = ["image/jpeg", "image/png", "image/webp"];

export default function ThemTaiKhoan() {
  const [form, setForm] = useState({
    tenNguoiNhan: "",
    nganHang: "",
    soTaiKhoan: "",
    qrFile: null,
  });

  const [touched, setTouched] = useState({});
  const [qrPreview, setQrPreview] = useState(null);
  const [message, setMessage] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [showPreview, setShowPreview] = useState(false);
  const [dragOver, setDragOver] = useState(false);
  const [daCoTaiKhoan, setDaCoTaiKhoan] = useState(false);

  const navigate = useNavigate();
  const fileInputRef = useRef(null);

  useEffect(() => {
    const kiemTraTaiKhoan = async () => {
      try {
        const res = await authApis().get(endpoints["tk-nganhang-cua-toi"]);
        if (res.data && Object.keys(res.data).length > 0) {
          setDaCoTaiKhoan(true);
        }
      } catch (err) {
        console.error("Lỗi kiểm tra tài khoản:", err);
      }
    };
    kiemTraTaiKhoan();
  }, []);

  const setField = (name, value) => setForm(prev => ({ ...prev, [name]: value }));

  const errors = {
    tenNguoiNhan: !form.tenNguoiNhan.trim() ? "Vui lòng nhập tên người nhận" : form.tenNguoiNhan.trim().length < 2 ? "Tên quá ngắn" : "",
    nganHang: !form.nganHang ? "Vui lòng chọn ngân hàng" : "",
    soTaiKhoan: !form.soTaiKhoan
      ? "Vui lòng nhập số tài khoản"
      : /[^0-9]/.test(form.soTaiKhoan)
        ? "Chỉ dùng ký tự số"
        : form.soTaiKhoan.length < 6
          ? "Tối thiểu 6 số"
          : "",
    qrFile: !form.qrFile
      ? "Vui lòng tải ảnh QR"
      : !ACCEPT_TYPES.includes(form.qrFile.type)
        ? "Ảnh phải là JPG/PNG/WEBP"
        : form.qrFile.size > MAX_FILE_SIZE
          ? "Tối đa 5MB"
          : "",
  };

  const isValid = Object.values(errors).every(e => e === "");

  const handleBlur = (e) => setTouched(prev => ({ ...prev, [e.target.name]: true }));

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "soTaiKhoan") setField(name, value.replace(/\s+/g, ""));
    else setField(name, value);
  };

  const readPreview = (file) => {
    if (!file) return setQrPreview(null);
    const reader = new FileReader();
    reader.onload = (e) => setQrPreview(e.target.result);
    reader.readAsDataURL(file);
  };

  const acceptFile = (file) => {
    if (!file) return;
    if (!ACCEPT_TYPES.includes(file.type)) {
      setMessage({ type: "danger", text: "Ảnh phải là JPG/PNG/WEBP" });
      return;
    }
    if (file.size > MAX_FILE_SIZE) {
      setMessage({ type: "danger", text: "Ảnh tối đa 5MB" });
      return;
    }
    setField("qrFile", file);
    readPreview(file);
  };

  const onFileInputChange = (e) => {
    const file = e.target.files?.[0];
    acceptFile(file);
  };

  const onDragOver = (e) => { e.preventDefault(); setDragOver(true); };
  const onDragLeave = () => setDragOver(false);
  const onDrop = (e) => {
    e.preventDefault();
    setDragOver(false);
    const file = e.dataTransfer.files?.[0];
    acceptFile(file);
  };

  const removeQr = () => {
    setField("qrFile", null);
    setQrPreview(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setTouched({ tenNguoiNhan: true, nganHang: true, soTaiKhoan: true, qrFile: true });
    if (!isValid) return;

    setSubmitting(true);
    setMessage(null);
    setUploadProgress(0);

    try {
      const fd = new FormData();
      fd.append("tenNguoiNhan", form.tenNguoiNhan.trim());
      fd.append("nganHang", form.nganHang);
      fd.append("soTaiKhoan", form.soTaiKhoan);
      fd.append("qrFile", form.qrFile);

      await authApis().post(endpoints["them-tk-nganhang"], fd, {
        headers: { "Content-Type": "multipart/form-data" },
        onUploadProgress: (evt) => {
          if (!evt.total) return;
          const pct = Math.round((evt.loaded * 100) / evt.total);
          setUploadProgress(pct);
        },
      });

      setMessage({ type: "success", text: "Thêm tài khoản thành công!" });
      setTimeout(() => navigate("/thongtincanhan"), 1000);
    } catch (err) {
      console.error(err);
      const detail = err?.response?.data?.message || "Thêm thất bại!";
      setMessage({ type: "danger", text: detail });
    } finally {
      setSubmitting(false);
    }
  };

  if (daCoTaiKhoan) {
    return (
      <Container className="mt-5" style={{ maxWidth: 600 }}>
        <Alert variant="info">
          Bạn đã có tài khoản ngân hàng. Không thể thêm mới.
        </Alert>
      </Container>
    );
  }

  return (
    <Container className="py-4" style={{ maxWidth: 980 }}>
      <Row className="mb-3">
        <Col>
          <h3 className="mb-1">Thêm tài khoản ngân hàng</h3>
          <div className="text-muted">Điền thông tin nhận chuyển khoản để hiển thị cho người thắng đấu giá.</div>
        </Col>
      </Row>

      <Card className="shadow-sm border-0">
        <Card.Body className="p-4">
          <Form onSubmit={handleSubmit} noValidate>
            <Row className="g-4">
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Tên người nhận</Form.Label>
                  <Form.Control
                    name="tenNguoiNhan"
                    placeholder="VD: Nguyen Van A"
                    value={form.tenNguoiNhan}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    isInvalid={touched.tenNguoiNhan && !!errors.tenNguoiNhan}
                    autoComplete="name"
                  />
                  <Form.Control.Feedback type="invalid">{errors.tenNguoiNhan}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Ngân hàng</Form.Label>
                  <Form.Select
                    name="nganHang"
                    value={form.nganHang}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    isInvalid={touched.nganHang && !!errors.nganHang}
                  >
                    <option value="">-- Chọn ngân hàng --</option>
                    {NGAN_HANGS.map((b, i) => (
                      <option key={i} value={b}>{b}</option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">{errors.nganHang}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Số tài khoản</Form.Label>
                  <InputGroup>
                    <Form.Control
                      name="soTaiKhoan"
                      inputMode="numeric"
                      placeholder="Chỉ nhập số"
                      value={form.soTaiKhoan}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      isInvalid={touched.soTaiKhoan && !!errors.soTaiKhoan}
                      maxLength={20}
                    />
                  </InputGroup>
                  <Form.Control.Feedback type="invalid">{errors.soTaiKhoan}</Form.Control.Feedback>
                </Form.Group>

                <div className="d-flex gap-2 mt-2">
                  <Button type="submit" variant="success" disabled={submitting || !isValid}>
                    {submitting ? <><Spinner animation="border" size="sm" className="me-2" />Đang lưu...</> : "Lưu thông tin"}
                  </Button>
                  <Button variant="outline-secondary" type="button" onClick={() => navigate(-1)} disabled={submitting}>
                    Hủy
                  </Button>
                </div>

                {submitting && <ProgressBar className="mt-3" now={uploadProgress} animated label={`${uploadProgress}%`} />}
              </Col>

              <Col md={6}>
                <Form.Label>Ảnh QR (nhận chuyển khoản)</Form.Label>
                <div
                  role="button"
                  onClick={() => fileInputRef.current?.click()}
                  onDragOver={onDragOver}
                  onDragLeave={onDragLeave}
                  onDrop={onDrop}
                  className={`border rounded-4 p-4 text-center ${dragOver ? "bg-light border-primary" : "bg-body"}`}
                  style={{ cursor: "pointer", minHeight: 220, display: "flex", alignItems: "center", justifyContent: "center" }}
                >
                  {!qrPreview ? (
                    <div>
                      <div className="fw-semibold mb-1">Kéo & thả ảnh QR vào đây</div>
                      <div className="text-muted mb-3">hoặc bấm để chọn (JPG/PNG/WEBP, ≤ 5MB)</div>
                      <Button size="sm" variant="outline-primary">Chọn ảnh</Button>
                    </div>
                  ) : (
                    <div className="w-100 d-flex flex-column align-items-center">
                      <Image src={qrPreview} alt="QR Preview" rounded style={{ maxWidth: 260, maxHeight: 260, objectFit: "contain" }} onClick={(e) => { e.stopPropagation(); setShowPreview(true); }} />
                      <div className="d-flex gap-2 mt-3">
                        <Button size="sm" variant="outline-primary" onClick={(e) => { e.stopPropagation(); fileInputRef.current?.click(); }}>Đổi ảnh</Button>
                        <Button size="sm" variant="outline-danger" onClick={(e) => { e.stopPropagation(); removeQr(); }}>Xóa</Button>
                      </div>
                    </div>
                  )}
                  <Form.Control
                    ref={fileInputRef}
                    type="file"
                    accept={ACCEPT_TYPES.join(",")}
                    onChange={onFileInputChange}
                    style={{ display: "none" }}
                  />
                </div>
                {touched.qrFile && errors.qrFile && (
                  <div className="invalid-feedback d-block mt-2">{errors.qrFile}</div>
                )}
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      <ToastContainer position="top-end" className="p-3">
        {message && (
          <Toast bg={message.type === "success" ? "success" : "danger"} onClose={() => setMessage(null)} show delay={3000} autohide>
            <Toast.Header closeButton>
              <strong className="me-auto">{message.type === "success" ? "Thành công" : "Lỗi"}</strong>
              <small>Hệ thống</small>
            </Toast.Header>
            <Toast.Body className="text-white">{message.text}</Toast.Body>
          </Toast>
        )}
      </ToastContainer>

      <Modal show={showPreview} onHide={() => setShowPreview(false)} centered size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Xem ảnh QR</Modal.Title>
        </Modal.Header>
        <Modal.Body className="d-flex justify-content-center">
          {qrPreview && <Image src={qrPreview} alt="QR Preview Large" fluid style={{ maxHeight: "70vh", objectFit: "contain" }} />}
        </Modal.Body>
      </Modal>
    </Container>
  );
}
