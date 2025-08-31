import { Button, Card, Image, Upload, UploadFile, UploadProps } from "antd";
import React, { useState } from "react";
import { AiOutlineDelete } from "react-icons/ai";
import { IoCloudUploadOutline } from "react-icons/io5";
import { UploadUtils } from "../../utils/UploadUtils.ts";
import { createErrorNotification } from "../notification/Notification.ts";

type FileType = Parameters<NonNullable<UploadProps['beforeUpload']>>[0];

interface ImageUploadProps {
    fileList: UploadFile[];
    onFileChange: (fileList: UploadFile[], files: File[]) => void;
    disabled?: boolean;
    acceptedTypes?: string[];
    multiple?: boolean;
}

const ImageUpload: React.FC<ImageUploadProps> = ({
                                                     fileList,
                                                     onFileChange,
                                                     disabled = false,
                                                     acceptedTypes = ["image/jpeg", "image/png"],
                                                     multiple = false,
                                                 }) => {
    const [previewImage, setPreviewImage] = useState("");
    const [previewOpen, setPreviewOpen] = useState(false);

    /** Convert file to base64 string */
    const getBase64Image = async (file: UploadFile): Promise<string> => {
        let src = file.url as string;
        if (!src && file.originFileObj) {
            src = await new Promise<string>((resolve) => {
                const reader = new FileReader();
                reader.readAsDataURL(file.originFileObj as FileType);
                reader.onload = () => resolve(reader.result as string);
            });
        } else {
            const thumbUrl = file.thumbUrl as string || "";
            src = UploadUtils.arrayBufferToBase64(thumbUrl) || "";
        }
        return src;
    };

    /** Handle change */
    const onChange: UploadProps["onChange"] = async ({fileList: newFileList}) => {
        // Convert each file to base64 preview if not already set
        const updatedFileList = await Promise.all(
                newFileList.map(async (file) => {
                    if (!file.thumbUrl && file.originFileObj) {
                        file.thumbUrl = await getBase64Image(file);
                    }
                    return file;
                })
        );

        const selectedFiles = updatedFileList
                .map((file) => file.originFileObj as File)
                .filter(Boolean);

        onFileChange(updatedFileList, selectedFiles);
    };


    /** Validate file before upload */
    const beforeUpload = (file: File) => {
        if (!acceptedTypes.includes(file.type)) {
            const acceptedTypesString = acceptedTypes
                    .map((t) => t.split("/")[1].toUpperCase())
                    .join("/");
            createErrorNotification(
                    "Invalid File Type",
                    `You can only upload ${acceptedTypesString} files!`
            );
            return Upload.LIST_IGNORE;
        }
        return false; // prevent auto-upload
    };

    /** Preview image */
    const onPreview = async (file: UploadFile) => {
        setPreviewOpen(true);
        const src = await getBase64Image(file);
        setPreviewImage(src);
    };

    /** Delete file */
    const onDelete = (file: UploadFile) => {
        const newFileList = fileList.filter((item) => item.uid !== file.uid);
        onFileChange(newFileList, []);
    };

    return (
            <>
                <Upload
                        fileList={fileList}
                        onChange={onChange}
                        beforeUpload={beforeUpload}
                        disabled={disabled || (!multiple && fileList.length > 0)}
                        showUploadList={false}
                >
                    <Button icon={<IoCloudUploadOutline/>}>Upload</Button>
                </Upload>

                <div className="w-full !mt-2">
                    {fileList.map((file) => (
                            <Card key={file.uid} hoverable={!disabled} className="mt-2 mb-2">
                                <div className="w-full flex items-center justify-between">
                                    <div className="flex-1 flex items-center gap-3 cursor-pointer"
                                         onClick={() => onPreview(file)}>
                                        <img src={UploadUtils.base64ToArrayBuffer(file.thumbUrl || '')} alt={file.name} width={20}/>
                                        <span className="text-[15px] truncate">{file.name}</span>
                                    </div>
                                    <AiOutlineDelete
                                            onClick={() => onDelete(file)}
                                            className="text-red-500 text-xl cursor-pointer"
                                    />
                                </div>
                            </Card>
                    ))}
                </div>

                {previewImage && (
                        <Image
                                wrapperStyle={{display: "none"}}
                                preview={{
                                    visible: previewOpen,
                                    onVisibleChange: setPreviewOpen,
                                    afterOpenChange: (visible) => !visible && setPreviewImage(""),
                                }}
                                src={previewImage}
                        />
                )}
            </>
    );
};

export default ImageUpload;
