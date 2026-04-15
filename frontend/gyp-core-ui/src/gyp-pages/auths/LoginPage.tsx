import { Alert, Button, Form, Input } from "antd";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import AuthLayout from "../../components/auth/AuthLayout.tsx";
import GoogleLoginButton from "../../components/auth/GoogleLoginButton.tsx";
import PasswordInput from "../../components/auth/PasswordInput.tsx";
import { useCustomerAuth } from "../../hooks/auth/useCustomerAuth.ts";
import { CustomerLoginRequestDto } from "../../models/generated/auth-service-models";
import { CustomerAuthStorage } from "../../services/CustomerAuth/CustomerAuthStorage.ts";

const LoginPage: React.FC = () => {
	const authShellClassName = [
		"[&_.customer-auth-shell]:relative",
		"[&_.customer-auth-shell]:min-h-screen",
		"[&_.customer-auth-shell]:overflow-hidden",
		"[&_.customer-auth-shell]:bg-[radial-gradient(circle_at_top_left,rgba(237,171,51,0.18),transparent_28%),radial-gradient(circle_at_bottom_right,rgba(36,121,177,0.24),transparent_32%),linear-gradient(135deg,#08111f_0%,#0f172a_45%,#152238_100%)]",
		"[&_.customer-auth-shell]:px-4",
		"[&_.customer-auth-shell]:py-8",
		"sm:[&_.customer-auth-shell]:px-6",
		"lg:[&_.customer-auth-shell]:px-8",
		"[&_.customer-auth-shell]:text-slate-50",
		"[&_.customer-auth-orb]:absolute",
		"[&_.customer-auth-orb]:pointer-events-none",
		"[&_.customer-auth-orb]:rounded-full",
		"[&_.customer-auth-orb]:blur-[20px]",
		"[&_.customer-auth-orb]:opacity-[0.55]",
		"[&_.customer-auth-orb-one]:h-[280px]",
		"[&_.customer-auth-orb-one]:w-[280px]",
		"[&_.customer-auth-orb-one]:-top-20",
		"[&_.customer-auth-orb-one]:-left-16",
		"[&_.customer-auth-orb-one]:bg-[rgba(250,204,21,0.24)]",
		"[&_.customer-auth-orb-two]:h-[340px]",
		"[&_.customer-auth-orb-two]:w-[340px]",
		"[&_.customer-auth-orb-two]:-right-24",
		"[&_.customer-auth-orb-two]:-bottom-28",
		"[&_.customer-auth-orb-two]:bg-[rgba(59,130,246,0.22)]",
		"[&_.customer-auth-grid]:relative",
		"[&_.customer-auth-grid]:z-10",
		"[&_.customer-auth-grid]:mx-auto",
		"[&_.customer-auth-grid]:grid",
		"[&_.customer-auth-grid]:min-h-[calc(100vh-64px)]",
		"[&_.customer-auth-grid]:max-w-[1180px]",
		"[&_.customer-auth-grid]:items-center",
		"[&_.customer-auth-grid]:gap-7",
		"lg:[&_.customer-auth-grid]:grid-cols-[minmax(0,1.1fr)_minmax(360px,0.9fr)]",
		"[&_.customer-auth-hero]:flex",
		"[&_.customer-auth-hero]:min-h-[620px]",
		"[&_.customer-auth-hero]:flex-col",
		"[&_.customer-auth-hero]:justify-between",
		"[&_.customer-auth-hero]:rounded-[28px]",
		"[&_.customer-auth-hero]:border",
		"[&_.customer-auth-hero]:border-white/10",
		"[&_.customer-auth-hero]:bg-[linear-gradient(160deg,rgba(14,22,36,0.88),rgba(10,18,32,0.7))]",
		"[&_.customer-auth-hero]:p-10",
		"[&_.customer-auth-hero]:shadow-[0_30px_80px_rgba(2,6,23,0.42)]",
		"[&_.customer-auth-hero]:backdrop-blur-[22px]",
		"[&_.customer-auth-card]:rounded-[28px]",
		"[&_.customer-auth-card]:border",
		"[&_.customer-auth-card]:border-white/10",
		"[&_.customer-auth-card]:bg-[rgba(8,15,28,0.72)]",
		"[&_.customer-auth-card]:p-4",
		"[&_.customer-auth-card]:shadow-[0_30px_80px_rgba(2,6,23,0.42)]",
		"[&_.customer-auth-card]:backdrop-blur-[22px]",
		"[&_.customer-auth-panel]:rounded-[22px]",
		"[&_.customer-auth-panel]:border",
		"[&_.customer-auth-panel]:border-slate-600/20",
		"[&_.customer-auth-panel]:bg-[linear-gradient(180deg,rgba(15,23,42,0.98),rgba(9,14,28,0.96))]",
		"[&_.customer-auth-panel]:p-7",
		"[&_.customer-auth-panel]:shadow-[inset_0_1px_0_rgba(255,255,255,0.04)]",
		"[&_.customer-auth-brand]:mb-7",
		"[&_.customer-auth-brand]:flex",
		"[&_.customer-auth-brand]:items-start",
		"[&_.customer-auth-brand]:gap-4",
		"[&_.customer-auth-brand-mark]:grid",
		"[&_.customer-auth-brand-mark]:h-16",
		"[&_.customer-auth-brand-mark]:w-16",
		"[&_.customer-auth-brand-mark]:place-items-center",
		"[&_.customer-auth-brand-mark]:rounded-[18px]",
		"[&_.customer-auth-brand-mark]:bg-gradient-to-br",
		"[&_.customer-auth-brand-mark]:from-yellow-300",
		"[&_.customer-auth-brand-mark]:to-amber-500",
		"[&_.customer-auth-brand-mark]:font-black",
		"[&_.customer-auth-brand-mark]:tracking-[0.14em]",
		"[&_.customer-auth-brand-mark]:text-slate-900",
		"[&_.customer-auth-brand-mark]:shadow-[0_18px_40px_rgba(245,158,11,0.35)]",
		"[&_.customer-auth-kicker]:mb-2",
		"[&_.customer-auth-kicker]:text-[0.76rem]",
		"[&_.customer-auth-kicker]:font-bold",
		"[&_.customer-auth-kicker]:uppercase",
		"[&_.customer-auth-kicker]:tracking-[0.22em]",
		"[&_.customer-auth-kicker]:text-amber-400",
		"[&_.customer-auth-hero_h1]:max-w-[11ch]",
		"[&_.customer-auth-hero_h1]:text-[clamp(2.4rem,4vw,4.8rem)]",
		"[&_.customer-auth-hero_h1]:font-black",
		"[&_.customer-auth-hero_h1]:leading-[0.96]",
		"[&_.customer-auth-hero-copy]:mt-5",
		"[&_.customer-auth-hero-copy]:max-w-[56ch]",
		"[&_.customer-auth-hero-copy]:text-[1.02rem]",
		"[&_.customer-auth-hero-copy]:leading-7",
		"[&_.customer-auth-hero-copy]:text-slate-200/85",
		"[&_.customer-auth-stat-grid]:mt-7",
		"[&_.customer-auth-stat-grid]:grid",
		"[&_.customer-auth-stat-grid]:gap-3.5",
		"lg:[&_.customer-auth-stat-grid]:grid-cols-3",
		"[&_.customer-auth-stat-card]:flex",
		"[&_.customer-auth-stat-card]:min-h-32",
		"[&_.customer-auth-stat-card]:flex-col",
		"[&_.customer-auth-stat-card]:gap-3",
		"[&_.customer-auth-stat-card]:rounded-[18px]",
		"[&_.customer-auth-stat-card]:border",
		"[&_.customer-auth-stat-card]:border-slate-400/15",
		"[&_.customer-auth-stat-card]:bg-slate-900/70",
		"[&_.customer-auth-stat-card]:p-4",
		"[&_.customer-auth-stat-card]:text-slate-100/90",
	].join(" ");

	const [form] = Form.useForm();
	const [searchParams] = useSearchParams();
	const [loading, setLoading] = useState(false);
	const [errorMessage, setErrorMessage] = useState<string | null>(null);
	const [bannerMessage, setBannerMessage] = useState<string | null>(null);
	const [bannerType, setBannerType] = useState<"success" | "error" | null>(null);
	const navigate = useNavigate();
	const {login, saveSession} = useCustomerAuth();

	useEffect(() => {
		const redirect = searchParams.get("redirect");
		if(redirect) {
			CustomerAuthStorage.setRedirectPath(redirect);
		}

		const success = searchParams.get("success");
		const verified = searchParams.get("verified");
		const registered = searchParams.get("registered");
		const reset = searchParams.get("reset");
		const oauth2Error = searchParams.get("oauth2Error");

		if(success === "verified" || verified === "verified") {
			setBannerType("success");
			setBannerMessage("Your email has been verified. You can sign in now.");
		} else if(success === "failed" || verified === "failed") {
			setBannerType("error");
			setBannerMessage("Email verification failed. Please request a new verification link.");
		} else if(registered === "1") {
			setBannerType("success");
			setBannerMessage("Registration submitted. Check your email to verify your account.");
		} else if(reset === "success") {
			setBannerType("success");
			setBannerMessage("Your password was reset successfully. Sign in with the new password.");
		} else if(oauth2Error) {
			setBannerType("error");
			setBannerMessage(oauth2Error);
		}
	}, [searchParams]);

	const handleGoogleLogin = (): void => {
		const redirectPath = searchParams.get("redirect") || CustomerAuthStorage.getRedirectPath() || "/gyp/";
		CustomerAuthStorage.setRedirectPath(redirectPath);
		window.location.href = `http://localhost:9999/auths/customer/oauth2/authorize/google-customer`;
	};

	const onFinish = async (values: CustomerLoginRequestDto): Promise<void> => {
		setLoading(true);
		setErrorMessage(null);
		try {
			const response = await login(values);
			saveSession(response);
			navigate(CustomerAuthStorage.consumeRedirectPath() || "/gyp/");
		} catch(error: unknown) {
			if(axios.isAxiosError(error)) {
				setErrorMessage(error?.response?.data?.message || error?.response?.data || "Login failed. Please try again.");
			}
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className={authShellClassName}>
			<AuthLayout
				title="Sign in"
				description="Use your customer account to buy tickets, track orders, and recover your sessions with email or Google."
				footer={
					<div className="mt-4 text-center text-sm leading-6 text-slate-200/80">
						<span>Don&apos;t have an account? </span>
						<Link className="font-semibold text-amber-400 transition hover:text-amber-300" to="/register">
							Create one here
						</Link>
					</div>
				}
			>
				{bannerMessage && bannerType && (
					<Alert
						className="rounded-xl border border-white/10 bg-slate-900/85 text-slate-50 shadow-lg [&_.ant-alert-message]:text-slate-50 [&_.ant-alert-description]:text-slate-200"
						type={bannerType}
						message={bannerType === "success" ? "Welcome back" : "Sign in notice"}
						description={bannerMessage}
						showIcon
					/>
				)}
				{errorMessage && (
					<Alert
						className="rounded-xl border border-white/10 bg-slate-900/85 text-slate-50 shadow-lg [&_.ant-alert-message]:text-slate-50 [&_.ant-alert-description]:text-slate-200"
						type="error"
						message="Sign in failed"
						description={errorMessage}
						showIcon
					/>
				)}
				<Form
					form={form}
					layout="vertical"
					className="mt-5 grid gap-4 [&_.ant-form-item]:!mb-0 [&_.ant-form-item-label>label]:font-semibold [&_.ant-form-item-label>label]:text-slate-200 [&_.ant-form-item-explain-error]:mt-1 [&_.ant-form-item-explain-error]:text-rose-400"
					onFinish={onFinish}
					requiredMark={false}
				>
					<Form.Item
						name="email"
						label="Email"
						rules={[
							{required: true, message: "Please enter your email"},
							{type: "email", message: "Please enter a valid email"},
						]}
					>
						<Input className="h-12 rounded-xl border-slate-700 bg-slate-900/85 text-slate-50 placeholder:text-slate-400 hover:border-amber-400" placeholder="name@example.com" autoComplete="email" />
					</Form.Item>

					<Form.Item
						name="password"
						label="Password"
						rules={[{required: true, message: "Please enter your password"}]}
					>
						<PasswordInput className="h-12 rounded-xl border-slate-700 bg-slate-900/85 text-slate-50 placeholder:text-slate-400 hover:border-amber-400" placeholder="Your password" autoComplete="current-password" />
					</Form.Item>

					<div className="mt-1 flex flex-wrap justify-between gap-3 text-sm text-slate-300/80">
						<Link className="font-medium text-amber-400 transition hover:text-amber-300" to="/forgot-password">
							Forgot password?
						</Link>
						<span>We keep your session token separate from internal users.</span>
					</div>

					<div className="mt-2 grid gap-3">
						<Button className="h-12 rounded-xl border-0 bg-gradient-to-r from-amber-400 to-amber-500 font-extrabold tracking-wide text-slate-950 shadow-lg shadow-amber-500/20 hover:from-amber-300 hover:to-amber-500 hover:text-slate-950" type="primary" htmlType="submit" loading={loading} block>
							Sign in
						</Button>
						<div className="[&_.customer-google-button]:h-12 [&_.customer-google-button]:rounded-xl [&_.customer-google-button]:border [&_.customer-google-button]:border-slate-600/30 [&_.customer-google-button]:bg-slate-900/92 [&_.customer-google-button]:font-semibold [&_.customer-google-button]:text-slate-100 [&_.customer-google-button]:shadow-none [&_.customer-google-button]:hover:border-amber-400/40">
							<GoogleLoginButton onClick={handleGoogleLogin} loading={loading} />
						</div>
					</div>
				</Form>
				<div className="mt-4 text-center text-sm leading-6 text-slate-200/80">
					<span>Need a new account? </span>
					<Link className="font-semibold text-amber-400 transition hover:text-amber-300" to="/register">
						Register
					</Link>
				</div>
			</AuthLayout>
		</div>
	);
};

export default LoginPage;