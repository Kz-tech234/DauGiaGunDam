import { useState, useContext } from "react";
import { Form, Button, Alert, Container } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";
import cookie from "react-cookies";
import Apis, { authApis, endpoints } from "../configs/Apis";
import { MyDispatchContext } from "../configs/Contexts";
import MySpinner from "./layout/MySpinner";
import '../css/DangNhap.css';

const DangNhap = () => {
    const [user, setUser] = useState({});
    const [msg, setMsg] = useState(null);
    const [loading, setLoading] = useState(false);
    const dispatch = useContext(MyDispatchContext);
    const [q] = useSearchParams();
    const nav = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            let res = await Apis.post(endpoints['login'], user);
            cookie.save("token", res.data.token);
            localStorage.setItem("token", res.data.token);

            let u = await authApis().get(endpoints['current-user']);
            dispatch({ type: "login", payload: u.data });

            let next = q.get("next");
            nav(next || "/");
        } catch (err) {
            console.error(err);
            setMsg("Đăng nhập thất bại!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="login-container">
            <h2 className="text-center mb-4">Đăng nhập</h2>
            {msg && <Alert variant="danger">{msg}</Alert>}

            <Form onSubmit={handleSubmit} className="form-style">
                <Form.Group className="mb-3">
                    <Form.Control
                        type="text"
                        placeholder="Tên đăng nhập"
                        value={user.username || ""}
                        onChange={e => setUser({ ...user, username: e.target.value })}
                        required
                        className="form-input"
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Control
                        type="password"
                        placeholder="Mật khẩu"
                        value={user.password || ""}
                        onChange={e => setUser({ ...user, password: e.target.value })}
                        required
                        className="form-input"
                    />
                </Form.Group>

                <div className="text-center">
                    {loading ? <MySpinner /> : <Button type="submit" variant="success" className="submit-button">Đăng nhập</Button>}
                </div>
            </Form>
        </Container>
    );
};

export default DangNhap;
