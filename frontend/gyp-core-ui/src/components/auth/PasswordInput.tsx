import { Input, InputProps } from "antd";

type PasswordInputProps = InputProps;

const PasswordInput = (props: PasswordInputProps) => {
	return <Input.Password {...props} visibilityToggle />;
};

export default PasswordInput;