package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageConfig extends BaseSeatMap implements Positionable, Dimensional, Rotatable {
	private Position position;
	private Dimension dimensions;
	private double rotation;
	private StageShape shape;
	private StageOrientation orientation;
	private String description;
	private boolean isActive = true;
	private List<Position> customVertices = new ArrayList<>(); // For custom section
	private double elevation = 0.0; // Height of stage (Optional)
	private String svgPath; // For define svg image

	public void setOrientation(StageOrientation orientation) {
		this.orientation = orientation;
		rotation = orientation.getAngle();
	}

	/**
	 * Thêm điểm đỉnh cho hình dạng custom
	 */
	public void addCustomVertex(Position vertex) {
		customVertices.add(vertex);
	}

	/**
	 * Tính toán điểm trung tâm của sân khấu
	 */
	public Position getCenterPosition() {
		if(Objects.equals(shape, StageShape.CUSTOM) && !customVertices.isEmpty()) {
			double sumX = customVertices.stream().mapToDouble(Position::getX).sum();
			double sumY = customVertices.stream().mapToDouble(Position::getY).sum();
			return new Position(
					position.getX() + sumX / customVertices.size(),
					position.getY() + sumY / customVertices.size()
			);
		}

		return new Position(
				position.getX() + dimensions.getWidth() / 2,
				position.getY() + dimensions.getHeight() / 2
		);
	}

	/**
	 * Tính toán khoảng cách từ một vị trí đến sân khấu
	 */
	public double calculateDistanceToStage(Position fromPosition) {
		Position stageCenter = getCenterPosition();
		double dx = fromPosition.getX() - stageCenter.getX();
		double dy = fromPosition.getY() - stageCenter.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Tính toán góc nhìn từ một vị trí đến sân khấu
	 */
	public double calculateViewingAngle(Position fromPosition) {
		Position stageCenter = getCenterPosition();
		double dx = stageCenter.getX() - fromPosition.getX();
		double dy = stageCenter.getY() - fromPosition.getY();
		double angle = Math.toDegrees(Math.atan2(dy, dx));

		// Chuẩn hóa góc từ 0-360 độ
		if(angle < 0) {
			angle += 360;
		}

		return angle;
	}

	/**
	 * Kiếm tra xem một vị trí có nằm trong vùng tầm nhìn tốt không
	 */
	public boolean isInOptimalViewingZone(Position seatPosition, double maxDistance, double optimalAngleRange) {
		double distance = calculateDistanceToStage(seatPosition);
		if(distance > maxDistance) {
			return false;
		}

		double viewingAngle = calculateViewingAngle(seatPosition);
		double stageAngle = orientation.getAngle();

		// Tính góc lệch so với hướng chính của sân khấu
		double angleDifference = Math.abs(viewingAngle - stageAngle);
		if(angleDifference > 180) {
			angleDifference = 360 - angleDifference;
		}

		return angleDifference <= optimalAngleRange / 2;
	}

	/**
	 * Tính điểm chất lượng cho một ghế dựa trên vị trí so với sân khấu
	 */
	public double calculateSeatQualityScore(Position seatPosition) {
		double distance = calculateDistanceToStage(seatPosition);
		double viewingAngle = calculateViewingAngle(seatPosition);
		double stageAngle = orientation.getAngle();

		// Tính điểm dựa trên khoảng cách (gần hơn = điểm cao hơn)
		double maxReasonableDistance = Math.max(dimensions.getWidth(), dimensions.getHeight()) * 10;
		double distanceScore = Math.max(0, (maxReasonableDistance - distance) / maxReasonableDistance);

		// Tính điểm dựa trên góc nhìn (chính diện = điểm cao hơn)
		double angleDifference = Math.abs(viewingAngle - stageAngle);
		if(angleDifference > 180) {
			angleDifference = 360 - angleDifference;
		}
		double angleScore = Math.max(0, (180 - angleDifference) / 180);

		// Trọng số: 60% khoảng cách, 40% góc nhìn
		return distanceScore * 0.6 + angleScore * 0.4;
	}

	/**
	 * Lấy các điểm biên của sân khấu (để vẽ)
	 */
	public List<Position> getBoundaryPoints() {
		List<Position> points = new ArrayList<>();
		double centerX, centerY, radius;

		switch(shape) {
			case RECTANGLE:
				points.add(new Position(position.getX(), position.getY()));
				points.add(new Position(position.getX() + dimensions.getWidth(), position.getY()));
				points.add(new Position(position.getX() + dimensions.getWidth(),
						position.getY() + dimensions.getHeight()));
				points.add(new Position(position.getX(), position.getY() + dimensions.getHeight()));
				break;

			case CIRCULAR:
				// Tạo các điểm trên đường tròn
				centerX = position.getX() + dimensions.getWidth() / 2;
				centerY = position.getY() + dimensions.getHeight() / 2;
				radius = Math.min(dimensions.getWidth(), dimensions.getHeight()) / 2;

				for(int i = 0; i < 36; i++) {
					double angle = i * 10; // Mỗi 10 độ
					double radians = Math.toRadians(angle);
					double x = centerX + radius * Math.cos(radians);
					double y = centerY + radius * Math.sin(radians);
					points.add(new Position(x, y));
				}
				break;

			case SEMICIRCLE:
				centerX = position.getX() + dimensions.getWidth() / 2;
				centerY = position.getY() + dimensions.getHeight();
				radius = Math.min(dimensions.getWidth(), dimensions.getHeight()) / 2;

				// Semi circle from 0 - 180
				for(int i = 0; i <= 18; i++) {
					double angle = i * 10;
					double radians = Math.toRadians(angle);
					double x = centerX + radius * Math.cos(radians);
					double y = centerY - radius * Math.sin(radians);
					points.add(new Position(x, y));
				}
				break;

			case CUSTOM:
				// Sử dụng các điểm đỉnh đã định nghĩa
				points = customVertices.stream().map(vertex -> Position.builder()
						.x(position.getX() + vertex.getX())
						.y(position.getY() + vertex.getY())
						.build()
				).collect(Collectors.toList());
				break;

			default:
				// Default is a Rectangle
				points.add(new Position(position.getX(), position.getY()));
				points.add(new Position(position.getX() + dimensions.getWidth(), position.getY()));
				points.add(new Position(position.getX() + dimensions.getWidth(),
						position.getY() + dimensions.getHeight()));
				points.add(new Position(position.getX(), position.getY() + dimensions.getHeight()));
				break;
		}
		return points;
	}
}