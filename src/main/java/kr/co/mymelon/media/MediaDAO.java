package kr.co.mymelon.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import kr.co.mymelon.mediagroup.MediagroupDTO;

@Repository
public class MediaDAO {
   
   @Autowired
   private JdbcTemplate jt;
   
   private StringBuilder sql = null;
   
   public MediaDAO() {
      System.out.println("-----MediaDAO() 객체생성됨");
   }//end
   
   public int create(MediaDTO mediaDto) {
      int cnt = 0;
      try {
         sql = new StringBuilder();
         
         sql.append(" INSERT INTO media(mediano, title, poster, filename, filesize, mediagroupno, rdate) ");
         sql.append(" VALUES( media_seq.nextval, ?, ?, ?, ?, ?, sysdate) ");
         
         cnt=jt.update(sql.toString(), mediaDto.getTitle(), mediaDto.getPoster(), mediaDto.getFilename(), mediaDto.getFilesize(), mediaDto.getMediagroupno());
         
      }catch (Exception e) {
         System.out.println("음원등록실패" + e);
      }//end
      
      return cnt;
   }//create() end
   
   public List<MediaDTO> list(int mediagroupno){
	      List<MediaDTO> list = null;
	      
	      try {
	         
	         sql = new StringBuilder();
	         sql.append(" SELECT mediano, title, poster, filename, filesize, mview, rdate, mediagroupno ");
	         sql.append(" FROM media ");
	         sql.append(" WHERE mview='Y' AND mediagroupno=" + mediagroupno);
	         sql.append(" ORDER BY mediano DESC ");
	         
	         RowMapper<MediaDTO> rowMapper = new RowMapper<MediaDTO>() {
	            
	            @Override
	            public MediaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	MediaDTO mediaDto = new MediaDTO();
	            	mediaDto.setMediano(rs.getInt("mediano"));
	            	mediaDto.setTitle(rs.getString("title"));
	            	mediaDto.setPoster(rs.getString("poster"));
	            	mediaDto.setFilename(rs.getString("filename"));
	            	mediaDto.setFilesize(rs.getInt("filesize"));
	            	mediaDto.setMview(rs.getString("mview"));
	            	mediaDto.setRdate(rs.getString("rdate"));
	            	mediaDto.setMediagroupno(rs.getInt("mediagroupno"));
	            	return mediaDto;
	            }//mapRow() end
	         };//rowMapper 익명 내부 객체
	         
	         list = jt.query(sql.toString(), rowMapper);
	         
	      }catch (Exception e) {
	         System.out.println("음원목록실패:" + e);
	      }//end
	      return list;
	   }//list() end
   
   public int totalRowCount() {
	      int cnt = 0;
	      try {
	         sql = new StringBuilder();
	         sql.append(" SELECT COUNT(*) FROM media ");
	         //SELECT를 실행했을 때 하나의 객체(Object) 결과 값이 나올 때 사용
	         cnt = jt.queryForObject(sql.toString(), Integer.class);
	      }catch (Exception e) {
	         System.out.println("음원전체행개수조회실패:" + e);
	      }//end
	      return cnt;
	   }//totalRowCount() end
   
   public int delete(int mediano) {
	      int cnt = 0;
	      try {
	         sql = new StringBuilder();
	         sql.append(" DELETE FROM media ");
	         sql.append(" WHERE mediano = ? ");
	         cnt = jt.update(sql.toString(), mediano);
	      }catch (Exception e) {
	         System.out.println("음원삭제실패:" + e);
	      }//end
	      return cnt;
	}//totalRowCount() end
   
   public MediaDTO read(int mediano){
	   MediaDTO mediaDto = null;
	      
	      try {
	         
	         sql = new StringBuilder();
	         sql.append(" SELECT mediano, title, poster, filename, filesize, mview, rdate, mediagroupno ");
	         sql.append(" FROM media ");
	         sql.append(" WHERE mediano = " + mediano);
	         
	         RowMapper<MediaDTO> rowMapper = new RowMapper<MediaDTO>() {
	            
	            @Override
	            public MediaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	                MediaDTO media = new MediaDTO();
	                media.setMediano(rs.getInt("mediano"));
	                media.setTitle(rs.getString("title"));
	                media.setPoster(rs.getString("poster"));
	                media.setFilename(rs.getString("filename"));
	                media.setFilesize(rs.getInt("filesize"));
	                media.setMview(rs.getString("mview"));
	                media.setRdate(rs.getString("rdate"));
	                media.setMediagroupno(rs.getInt("mediagroupno"));
	                return media;
	            }//mapRow() end
	         };//rowMapper 익명 내부 객체
	         
	         mediaDto = jt.queryForObject(sql.toString(), rowMapper);
	         
	      }catch (Exception e) {
	         System.out.println("상세보기실패:" + e);
	      }//end
	      return mediaDto;
	   }//list() end
   
	public int update(MediaDTO mediaDto) {
		int cnt=0;
		try {
		     sql = new StringBuilder();
		     sql.append(" UPDATE media ");
	         sql.append(" SET title=?, poster=?, filename=?, filesize=? ");
	         sql.append(" WHERE mediano = ? " );
	         
	         cnt = jt.update(sql.toString(), mediaDto.getTitle(), mediaDto.getPoster(), 
	        		 mediaDto.getFilename(), mediaDto.getFilesize(), mediaDto.getMediano());

		} catch (Exception e) {
			System.out.println("음원수정실패 : "+e);
		}//end
		return cnt;
	}//update() end
   
}//class end