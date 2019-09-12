package com.saki9.pojo;

/**
* @describe 弹幕
* @author saki9
* @date 2019年9月9日
* @version 1.0
 */
public class Danmaku {
	/** 起始时间 单位：秒 */
	private Long start;
	/** 结束时间 单位：秒 */
	private Long end;
	/** 弹幕类型 0：滚动弹幕，1：顶部弹幕，2：底部弹幕 */
	private Integer type;
	/** 弹幕样式 */
	private String style;
	/** 弹幕文本 */
	private String text;
	/** 弹幕颜色 */
	private String color;
	/** 弹幕高度 */
	private Integer height;
	/** 起始位置 */
	private Integer startPosition;
	/** 结束位置，非滚动弹幕无效 */
	private Integer endPosition;
	
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getEnd() {
		return end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}
	public Integer getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(Integer endPosition) {
		this.endPosition = endPosition;
	}
}
