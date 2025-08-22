import { Modal, Upload, UploadFile, UploadProps } from "antd";
import React from "react";
import { createErrorNotification } from "../notification/Notification.ts";

type FileType = Parameters<NonNullable<UploadProps['beforeUpload']>>[0];

interface ImageUploadProps {
    fileList: UploadFile[];
    onFileChange: (fileList: UploadFile[], files: File[]) => void;
    disabled?: boolean;
    maxCount?: number;
    maxSizeMB?: number;
    acceptedTypes?: string[];
    uploadText?: string;
}

const ImageUpload: React.FC<ImageUploadProps> = ({
                                                     fileList,
                                                     onFileChange,
                                                     disabled = false,
                                                     maxCount = 1,
                                                     maxSizeMB = 2,
                                                     acceptedTypes = ['image/jpeg', 'image/png'],
                                                     uploadText = '+ Upload'
                                                 }) => {
    const [modal, modalContextHolder] = Modal.useModal();

    // Custom upload onChange handler that prevents automatic upload
    const onChange: UploadProps['onChange'] = ({fileList: newFileList}) => {
        // Store all files for later upload
        const selectedFiles: File[] = [];
        newFileList.forEach(file => {
            if (file.originFileObj) {
                selectedFiles.push(file.originFileObj as File);
            }
        });

        onFileChange(newFileList, selectedFiles);
    };

    // Prevent automatic upload and validate file
    const beforeUpload = (file: File) => {
        // Validate file type
        const isValidType = acceptedTypes.includes(file.type);
        if (!isValidType) {
            const acceptedTypesString = acceptedTypes.map(type =>
                    type.split('/')[1].toUpperCase()
            ).join('/');
            createErrorNotification("Invalid File Type", `You can only upload ${acceptedTypesString} files!`);
            return false;
        }

        // Validate file size
        const isValidSize = file.size / 1024 / 1024 < maxSizeMB;
        if (!isValidSize) {
            createErrorNotification("File Size Error", `Image must be smaller than ${maxSizeMB}MB!`);
            return false;
        }

        // Return false to prevent automatic upload
        return false;
    };

    const onPreview = async (file: UploadFile) => {
        let src = file.url as string;
        if (!src && file.originFileObj) {
            src = await new Promise((resolve) => {
                const reader = new FileReader();
                reader.readAsDataURL(file.originFileObj as FileType);
                reader.onload = () => resolve(reader.result as string);
            });
        }

        modal.info({
            title: 'Image Preview',
            content: (
                    <div style={{textAlign: 'center'}}>
                        <img
                                src={src}
                                alt="Preview"
                                style={{maxWidth: '100%', maxHeight: '80vh'}}
                        />
                    </div>
            ),
            onOk() {
            },
            width: 600,
        });
    };

    return (
            <>
                <Upload
                        listType="picture-card"
                        fileList={fileList}
                        onChange={onChange}
                        onPreview={onPreview}
                        beforeUpload={beforeUpload}
                        disabled={disabled}
                        maxCount={maxCount}
                >
                    {fileList.length < maxCount && !disabled && uploadText}
                </Upload>
                {modalContextHolder}
            </>
    );
};

export default ImageUpload;