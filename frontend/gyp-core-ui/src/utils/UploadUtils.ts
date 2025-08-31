export class UploadUtils {
    static arrayBufferToBase64(buffer: string, mimeType: string = 'image/png'): string {
        return `data:${mimeType};base64,${buffer}`;
    }

    static base64ToArrayBuffer(base64: string, mimeType: string = 'image/png'): string {
        const prefix = `data:${mimeType};base64,`;
        if (base64.startsWith(prefix)) {
            return base64;
        } else {
            return prefix + base64;
        }
    }
}
