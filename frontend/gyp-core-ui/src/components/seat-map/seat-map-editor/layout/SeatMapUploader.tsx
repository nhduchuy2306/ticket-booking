import { Button, Upload, UploadFile } from "antd";
import React, { useEffect } from "react";
import { AiOutlineCloudUpload } from "react-icons/ai";

type SeatMapUploaderProps = {
    eventId: string;
};

const SeatMapUploader: React.FC<SeatMapUploaderProps> = (props) => {
    const fileList: UploadFile[] = [
        {
            uid: '0',
            name: 'xxx.png',
            status: 'uploading',
            percent: 33,
        },
        {
            uid: '-1',
            name: 'yyy.png',
            status: 'done',
            url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
            thumbUrl: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
        {
            uid: '-2',
            name: 'zzz.png',
            status: 'error',
        },
    ];

    const [eventId, setEventId] = React.useState<string>(props.eventId);

    useEffect(() => {
        const data = props.eventId;
        if (data) {
            setEventId(data);
        }
    }, [props.eventId]);

    const uploadAction = `https://660d2bd96ddfa2943b33731c.mockapi.io/api/upload/${eventId}`;

    return (
            <Upload action={uploadAction} listType="text" defaultFileList={fileList}>
                <Button type="primary" icon={<AiOutlineCloudUpload/>}>
                    Upload
                </Button>
            </Upload>
    );
}

export default SeatMapUploader;