package com.gyp.eventservice.dtos.seatmap2;

import java.util.List;

public class StageFactory {
	/**
	 * Tạo sân khấu hình chữ nhật cơ bản
	 */
	public static Stage createRectangleStage(String name, Position position, Dimension dimensions,
			StageOrientation orientation) {
		Stage stage = new Stage();
		stage.setName(name);
		stage.setPosition(position);
		stage.setDimensions(dimensions);
		stage.setShape(StageShape.RECTANGLE);
		stage.setOrientation(orientation);
		return stage;
	}

	/**
	 * Tạo sân khấu tròn
	 */
	public static Stage createCircularStage(String name, Position center, double radius, StageOrientation orientation) {
		Stage stage = new Stage();
		stage.setName(name);
		stage.setPosition(new Position(center.getX() - radius, center.getY() - radius));
		stage.setDimensions(new Dimension(radius * 2, radius * 2));
		stage.setShape(StageShape.CIRCULAR);
		stage.setOrientation(orientation);
		return stage;
	}

	/**
	 * Tạo sân khấu hình bán nguyệt
	 */
	public static Stage createSemicircleStage(String name, Position center, double radius,
			StageOrientation orientation) {
		Stage stage = new Stage();
		stage.setName(name);
		stage.setPosition(new Position(center.getX() - radius, center.getY() - radius));
		stage.setDimensions(new Dimension(radius * 2, radius));
		stage.setShape(StageShape.SEMICIRCLE);
		stage.setOrientation(orientation);
		return stage;
	}

	/**
	 * Tạo sân khấu Arena (giữa)
	 */
	public static Stage createArenaStage(String name, Position center, Dimension dimensions) {
		Stage stage = new Stage();
		stage.setName(name);
		stage.setPosition(new Position(
				center.getX() - dimensions.getWidth() / 2,
				center.getY() - dimensions.getHeight() / 2
		));
		stage.setDimensions(dimensions);
		stage.setShape(StageShape.ARENA);
		stage.setOrientation(StageOrientation.NORTH); // Arena không có hướng cụ thể
		return stage;
	}

	/**
	 * Tạo sân khấu custom với các điểm đỉnh
	 */
	public static Stage createCustomStage(String name, Position basePosition, List<Position> vertices,
			StageOrientation orientation) {
		Stage stage = new Stage();
		stage.setName(name);
		stage.setPosition(basePosition);
		stage.setShape(StageShape.CUSTOM);
		stage.setCustomVertices(vertices);
		stage.setOrientation(orientation);

		// Tính toán dimensions dựa trên vertices
		double minX = vertices.stream().mapToDouble(Position::getX).min().orElse(0);
		double maxX = vertices.stream().mapToDouble(Position::getX).max().orElse(0);
		double minY = vertices.stream().mapToDouble(Position::getY).min().orElse(0);
		double maxY = vertices.stream().mapToDouble(Position::getY).max().orElse(0);

		stage.setDimensions(new Dimension(maxX - minX, maxY - minY));

		return stage;
	}
}
