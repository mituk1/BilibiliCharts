package com.saki9.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
* @describe 视频信息
* @author saki9
* @date 2019年8月14日
* @version 1.0
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "biv_videoview", indexes={@Index(name="pubdate_idIndex",columnList="pubdate")})
public class VideoView extends IdEntity {
	/** avid */
	@Column(unique=true)
	private Long aid;
	/** 分区号 */
	private Long tid;
	/** 分类名 */
	private String tname;
	/** 观看统计 格式：[{"updtime":1565969497,"view":1054417,"danmaku":49604,"reply":43363,"favorite":29139,"coin":11030,"share":4409,"now_rank":0,"his_rank":0,"like":31782,"dislike":0}] */
	@Column(columnDefinition = "LongText")
	private String stat;
	/** 最新播放数 */
	private Long view;
	/** 最新弹幕数 */
	private Long danmaku;
	/** 最新评论数 */
	private Long reply;
	/** 最新收藏数 */
	private Long favorite;
	/** 最新硬币数 */
	private Long coin;
	/** 最新分享数 */
	private Long share;
	/** 最新点赞数 */
	@Column(name = "good_like")
	private Long like;
	/** 标题 */
	private String title;
	/** 简介 */
	@Column(columnDefinition = "LongText", name = "description")
	private String desc;
	/** 关键字 格式：[{"tag_id":6401936,"tag_name":"VTuber","cover":"http://i0.hdslb.com/bfs/archive/1fb59c4871f560d3ff69ac8d7dde01d07fd8873d.png","head_cover":"","content":"以虚拟形象自居创作的人们","short_content":"嗨~各位DD","type":3,"state":0,"ctime":1519800624,"count":{"view":0,"use":16495,"atten":15686},"is_atten":0,"likes":0,"hates":0,"attribute":0,"liked":0,"hated":0}] */
	@Column(columnDefinition = "LongText")
	private String tag;
	/** 封面图片URL地址 */
	private String pic;
	/** 投稿人mid */
	private Long mid;
	/** 投稿人昵称 */
	private String name;
	/** 投稿人头像URL */
	@Column(columnDefinition = "LongText")
	private String face;
	/** 视频发布时间 */
	private Date pubdate;
	/** 分区名称 */
	private String toptype;
	/** 分P数量 */
	private Integer pageSize;
	/** 分P信息, 其中cid表示视频源及弹幕编号 格式：[{"cid":83784715,"page":1,"from":"vupload","part":"Kizuna AI - AIAIAI (feat. 中田ヤスタカ)【Official Music Video】_x264","duration":194,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}}] */
	@Column(columnDefinition = "LongText")
	private String pages;
	
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public Long getView() {
		return view;
	}
	public void setView(Long view) {
		this.view = view;
	}
	public Long getDanmaku() {
		return danmaku;
	}
	public void setDanmaku(Long danmaku) {
		this.danmaku = danmaku;
	}
	public Long getReply() {
		return reply;
	}
	public void setReply(Long reply) {
		this.reply = reply;
	}
	public Long getFavorite() {
		return favorite;
	}
	public void setFavorite(Long favorite) {
		this.favorite = favorite;
	}
	public Long getCoin() {
		return coin;
	}
	public void setCoin(Long coin) {
		this.coin = coin;
	}
	public Long getShare() {
		return share;
	}
	public void setShare(Long share) {
		this.share = share;
	}
	public Long getLike() {
		return like;
	}
	public void setLike(Long like) {
		this.like = like;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public Date getPubdate() {
		return pubdate;
	}
	public void setPubdate(Date pubdate) {
		this.pubdate = pubdate;
	}
	public String getToptype() {
		return toptype;
	}
	public void setToptype(String toptype) {
		this.toptype = toptype;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
}
