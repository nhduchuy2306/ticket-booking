import { Button } from "antd";
import { FcGoogle } from "react-icons/fc";

interface GoogleLoginButtonProps {
	onClick: () => void;
	loading?: boolean;
	label?: string;
}

const GoogleLoginButton = ({onClick, loading = false, label = "Login with Google"}: GoogleLoginButtonProps) => {
	return (
		<Button className="customer-google-button" size="large" block onClick={onClick} loading={loading}>
			<FcGoogle size={20} />
			<span>{label}</span>
		</Button>
	);
};

export default GoogleLoginButton;