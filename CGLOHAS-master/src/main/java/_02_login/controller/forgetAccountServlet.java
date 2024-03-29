package _02_login.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import _00_init.GlobalService;
import _00_init.RegularExpressionUtil;
import _00_init.SendMailThread;
import _00_init.VerifyUtils;
import _01_register.model.register_DAO;
import _01_register.model.register_bean;
import _02_login.model.LoginIPBean;
import _02_login.model.LoginIPDAO;
import _02_login.model.LoginServiceDB;

@WebServlet("/_02_login/forgetAccount.do")
public class forgetAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		LoginIPDAO lidao = new LoginIPDAO();
		LoginIPBean lib = lidao.getIPBean(request.getRemoteAddr());
		boolean del = false;
		if (lib != null) {
			long lastTryTime = lib.getLastTryTime().getTime();
			long now = System.currentTimeMillis();
			long diff = now - lastTryTime;
			if (diff > (1000 * 60 * 60 * 3)) {
				lidao.delIP(lib);
				System.out.println("DEL");
				del = true;
			} else if (lib.getWrongTimes() == 3) {
				int hour = (int) diff / (1000 * 60 * 60);
				int min = (int) (diff - hour * 60) / (1000 * 60);
				response.sendRedirect(response.encodeRedirectURL("/CGLOHAS/_02_login/BlockError.jsp?ip="
						+ request.getRemoteAddr() + "&timeHour=" + hour + "&timeMin=" + min));
				return;
			}
		} else {
			// 第一次登入失敗 或登入失敗小於3 可執行查詢
		}

		Map<String, String> errorMsg = new HashMap<String, String>();
		request.setAttribute("MsgMap", errorMsg);
		// 1. 讀取使用者輸入資料
		String Password = request.getParameter("Password");
		String email = request.getParameter("Email");
		String Phone = request.getParameter("Phone");
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		if (gRecaptchaResponse == null || gRecaptchaResponse.trim().length() == 0) {
			errorMsg.put("errorRecaptcha", " 【Please verify】");
		} else {
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			if (valid == false) {
				errorMsg.put("errorRecaptcha", " 【Verify failed, Please re-verify】");
			}
		}
		if (Password == null || Password.trim().length() == 0) {
			errorMsg.put("errorPw", " 【Account must enter】");
		}
		if (email == null || email.trim().length() == 0) {
			errorMsg.put("errorEm", "  【E-mail must enter】");
		}
		if (Phone == null || Phone.trim().length() == 0) {
			errorMsg.put("errorPh", " 【Mobile must enter】");
		}
		if (!errorMsg.isEmpty()) {
			RequestDispatcher rd = request.getRequestDispatcher("forgetAccount.jsp");
			rd.forward(request, response);
			return;
		}
		List<String> erro = new ArrayList<>();
		register_DAO rs = new register_DAO();
		boolean finalCheck = true;

		if (RegularExpressionUtil.isPwValid(Password) == false) {
			erro.add("errorPw");
			finalCheck = false;
		}

		if (RegularExpressionUtil.isEmailValid(email) == false) {
			erro.add("ErrorEm");
			finalCheck = false;
		}

		if (RegularExpressionUtil.isPhoneValid(Phone) == false) {
			erro.add("ErrorPh");
			finalCheck = false;
		}

		register_bean rbcheck = null;

		if (finalCheck) {
			String GSpassword = GlobalService.encryptString(Password);
			String GSpassword1 = GlobalService.getMD5Endocing(GSpassword);
			rbcheck = rs.forgetAc(GSpassword1, email, Phone);

			if (rbcheck == null) {
				erro.add("ErrorPh");
			}
		}

		if (!erro.isEmpty()) {
			int timeLeft = 0;
			if (lib == null || del == true) {
				lib = new LoginIPBean(request.getRemoteAddr(), 1, new Timestamp(System.currentTimeMillis()));
				lidao.insertIP(lib);
				timeLeft = 2;
			} else {
				lib.setWrongTimes(lib.getWrongTimes() + 1);
				timeLeft = 3 - (lib.getWrongTimes());
				lib.setLastTryTime(new Timestamp(System.currentTimeMillis()));
			}
			if (timeLeft != 0) {
				request.setAttribute("timeLeft", timeLeft);
				RequestDispatcher rd = request.getRequestDispatcher("forgetAccount.jsp");
				rd.forward(request, response);
				return;
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("BlockError.jsp?ip=" + request.getRemoteAddr() + "&timeHour=3&timeMin=00"));
				return;
			}
		}

	
			SendMailThread smt = new SendMailThread(email, "", "Ac",rbcheck.getAccount() );
			smt.start();
			response.sendRedirect(response.encodeRedirectURL("OKsendEmail.jsp?from=ac"));
			return; 

	}
}
