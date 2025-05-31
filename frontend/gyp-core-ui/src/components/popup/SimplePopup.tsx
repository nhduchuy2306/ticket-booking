import { Modal } from "antd";
import React from "react";

interface SimplePopupProps {
    onCancel: () => void;
    onSave: () => void;
    open: boolean;
    content: () => HTMLElement;
}

const SimplePopup: React.FC<SimplePopupProps> = ({onCancel, onSave, open, content}) => {
    return (
            <>
                <Modal
                        open={open}
                        title="Title"
                        onOk={onSave}
                        onCancel={onCancel}
                        footer={(_, {OkBtn, CancelBtn}) => (
                                <>
                                    <CancelBtn/>
                                    <OkBtn/>
                                </>
                        )}
                >
                    <>{content}</>
                </Modal>
            </>
    );
}

export default SimplePopup;