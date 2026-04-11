// import Konva from "konva";
//
// export class SeatMapFunctionUtils {
//     public static handleResetView = (stage: Konva.Stage, layer: Konva.Layer) => {
//         const resetScale = 0.8;
//
//         // Set scale first
//         layer.scale({x: resetScale, y: resetScale});
//
//         // Calculate centered position
//         const newX = stageWidth / 2 - (stageWidth / 2) * resetScale;
//         const newY = stageHeight / 2 - (stageHeight / 2) * resetScale;
//
//         layer.position({x: newX, y: newY});
//         layer.batchDraw();
//
//         stage.position({x: 0, y: 0});
//         stage.batchDraw();
//
//         setZoomLevel(resetScale);
//         setShowSeatNumbers(false);
//     };
//
//     public static getZoomContext = (stage: Konva.Stage, layer: Konva.Layer) => {
//         const oldScale = layer.scaleX();
//         const pointer = stage.getPointerPosition();
//         if (!pointer) return null;
//
//         const mousePointTo = {
//             x: (pointer.x - layer.x()) / oldScale,
//             y: (pointer.y - layer.y()) / oldScale,
//         };
//
//         return {oldScale, pointer, mousePointTo};
//     };
//
//     public static applyZoom = (
//             scale: number,
//             pointer: { x: number; y: number },
//             mousePointTo: { x: number; y: number }
//     ) => {
//         if (!layerRef.current) return;
//
//         const clampedScale = Math.max(0.5, Math.min(3, scale));
//         layerRef.current.scale({x: clampedScale, y: clampedScale});
//         setZoomLevel(clampedScale);
//         setShowSeatNumbers(clampedScale > 1.7);
//
//         const newPos = {
//             x: pointer.x - mousePointTo.x * clampedScale,
//             y: pointer.y - mousePointTo.y * clampedScale,
//         };
//
//         layerRef.current.position(newPos);
//         layerRef.current.batchDraw();
//     };
//
//     public static handleWheel = (e: Konva.KonvaEventObject<WheelEvent>) => {
//         e.evt.preventDefault();
//         e.evt.stopPropagation();
//
//         const ctx = getZoomContext();
//         if (!ctx) return;
//         const {oldScale, pointer, mousePointTo} = ctx;
//
//         const direction = e.evt.deltaY > 0 ? -1 : 1;
//         const factor = 1.1;
//         const newScale = direction > 0 ? oldScale * factor : oldScale / factor;
//
//         this.applyZoom(newScale, pointer, mousePointTo);
//     };
//
//     public static handleZoomInOut = (isZoomIn: boolean) => {
//         const ctx = this.getZoomContext();
//         if (!ctx) return;
//         const {oldScale, pointer, mousePointTo} = ctx;
//
//         const factor = 1.1;
//         const newScale = isZoomIn ? oldScale * factor : oldScale / factor;
//
//         this.applyZoom(newScale, pointer, mousePointTo);
//     };
// }