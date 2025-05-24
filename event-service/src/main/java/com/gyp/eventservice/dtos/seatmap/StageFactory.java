package com.gyp.eventservice.dtos.seatmap;

import java.util.List;

public final class StageFactory {
	private StageFactory() {
	}

	/**
	 * Tạo sân khấu hình chữ nhật cơ bản
	 */
	public static StageConfig createRectangleStage(String name, Position position, Dimension dimensions,
			StageOrientation orientation, String description, boolean isActive, String svgPath) {
		return StageConfig.builder()
				.name(name)
				.position(position)
				.description(description)
				.dimensions(dimensions)
				.shape(StageShape.RECTANGLE)
				.orientation(orientation)
				.isActive(isActive)
				.svgPath(svgPath)
				.build();
	}

	/**
	 * Tạo sân khấu tròn
	 */
	public static StageConfig createCircularStage(String name, Position center, double radius,
			StageOrientation orientation) {
		StageConfig stageConfig = new StageConfig();
		stageConfig.setName(name);
		stageConfig.setPosition(new Position(center.getX() - radius, center.getY() - radius));
		stageConfig.setDimensions(new Dimension(radius * 2, radius * 2));
		stageConfig.setShape(StageShape.CIRCULAR);
		stageConfig.setOrientation(orientation);
		return stageConfig;
	}

	/**
	 * Tạo sân khấu hình bán nguyệt
	 */
	public static StageConfig createSemicircleStage(String name, Position center, double radius,
			StageOrientation orientation) {
		StageConfig stageConfig = new StageConfig();
		stageConfig.setName(name);
		stageConfig.setPosition(new Position(center.getX() - radius, center.getY() - radius));
		stageConfig.setDimensions(new Dimension(radius * 2, radius));
		stageConfig.setShape(StageShape.SEMICIRCLE);
		stageConfig.setOrientation(orientation);
		return stageConfig;
	}

	/**
	 * Tạo sân khấu Arena (giữa)
	 */
	public static StageConfig createArenaStage(String name, Position center, Dimension dimensions) {
		StageConfig stageConfig = new StageConfig();
		stageConfig.setName(name);
		stageConfig.setPosition(new Position(
				center.getX() - dimensions.getWidth() / 2,
				center.getY() - dimensions.getHeight() / 2
		));
		stageConfig.setDimensions(dimensions);
		stageConfig.setShape(StageShape.ARENA);
		stageConfig.setOrientation(StageOrientation.NORTH); // Arena không có hướng cụ thể
		return stageConfig;
	}

	/**
	 * Tạo sân khấu custom với các điểm đỉnh
	 */
	public static StageConfig createCustomStage(String name, Position basePosition, List<Position> vertices,
			StageOrientation orientation) {
		StageConfig stageConfig = new StageConfig();
		stageConfig.setName(name);
		stageConfig.setPosition(basePosition);
		stageConfig.setShape(StageShape.CUSTOM);
		stageConfig.setCustomVertices(vertices);
		stageConfig.setOrientation(orientation);

		// Tính toán dimensions dựa trên vertices
		double minX = vertices.stream().mapToDouble(Position::getX).min().orElse(0);
		double maxX = vertices.stream().mapToDouble(Position::getX).max().orElse(0);
		double minY = vertices.stream().mapToDouble(Position::getY).min().orElse(0);
		double maxY = vertices.stream().mapToDouble(Position::getY).max().orElse(0);

		stageConfig.setDimensions(new Dimension(maxX - minX, maxY - minY));

		return stageConfig;
	}
}
