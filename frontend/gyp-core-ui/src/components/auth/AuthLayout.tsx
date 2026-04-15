import { ReactNode } from "react";
import { FiCalendar, FiMail, FiShield } from "react-icons/fi";

interface AuthLayoutProps {
	title: string;
	description: string;
	children: ReactNode;
	footer?: ReactNode;
}

const AuthLayout = ({title, description, children, footer}: AuthLayoutProps) => {
	return (
		<div className="customer-auth-shell">
			<div className="customer-auth-orb customer-auth-orb-one" />
			<div className="customer-auth-orb customer-auth-orb-two" />
			<div className="customer-auth-grid">
				<section className="customer-auth-hero">
					<div className="customer-auth-brand">
						<div className="customer-auth-brand-mark">GYP</div>
						<div>
							<p className="customer-auth-kicker">Customer access</p>
							<h1>Book faster. Keep your tickets in one place.</h1>
						</div>
					</div>
					<p className="customer-auth-hero-copy">{description}</p>
					<div className="customer-auth-stat-grid">
						<div className="customer-auth-stat-card">
							<FiCalendar />
							<span>Live event availability</span>
						</div>
						<div className="customer-auth-stat-card">
							<FiShield />
							<span>Verified customer sessions</span>
						</div>
						<div className="customer-auth-stat-card">
							<FiMail />
							<span>Fast Google login and session restore</span>
						</div>
					</div>
				</section>
				<section className="customer-auth-card">
					<div className="customer-auth-panel">
						<div className="customer-auth-panel-heading">
							<p className="customer-auth-kicker">GYP Ticket Office</p>
							<h2>{title}</h2>
						</div>
						{children}
						{footer}
					</div>
				</section>
			</div>
		</div>
	);
};

export default AuthLayout;