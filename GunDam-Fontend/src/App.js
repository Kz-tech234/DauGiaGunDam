import { useReducer, useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Container } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';

import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";

import TrangChu from "./components/TrangChu";
import DangNhap from "./components/DangNhap";
import DangKy from "./components/DangKy";
import ThongTinCaNhan from "./components/ThongTinCaNhan";
import DangSanPham from "./components/DangSanPham";
import TrangDauGia from "./components/TrangDauGia";
import QuanLyBaiDau from "./components/QuanLyBaiDau";
import TrangDonThanhToan from "./components/TrangDonThanhToan";
import ThemTaiKhoan from "./components/ThemTaiKhoan";
import ChiTietBaiDauGia from "./components/ChiTietBaiDauGia";
import LienHe from "./components/LienHe";

import { MyUserContext, MyDispatchContext } from "./configs/Contexts";
import MyUserReducer from "./reducers/MyUserReducer";
import { authApis, endpoints } from "./configs/Apis";
import cookie from "react-cookies";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);

  useEffect(() => {
    const checkLogin = async () => {
      const token = cookie.load("token");
      if (token !== undefined) {
        try {
          let res = await authApis().get(endpoints['current-user']);
          dispatch({
            type: "login",
            payload: res.data
          });
        } catch (err) {
          cookie.remove("token");
        }
      }
    };
    checkLogin();
  }, []);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <Header />
          <Container>
            <Routes>
              <Route path="/" element={<TrangChu />} />
              <Route path="/dangnhap" element={<DangNhap />} />
              <Route path="/dangky" element={<DangKy />} />
              <Route path="/thongtincanhan" element={<ThongTinCaNhan />} />
              <Route path="/taodaugia" element={<DangSanPham />} />
              <Route path="/cuocdaugia" element={<TrangDauGia />} />
              <Route path="/quanlybaidau" element={<QuanLyBaiDau />} />
              <Route path="/thanhtoan" element={<TrangDonThanhToan />} />
              <Route path="/themtaikhoan" element={<ThemTaiKhoan />} />
              <Route path="/cuoc-dau-gia/:id" element={<ChiTietBaiDauGia />} />
              <Route path="/lienhe" element={<LienHe />} />
            </Routes>
          </Container>
          <Footer />
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
};

export default App;
