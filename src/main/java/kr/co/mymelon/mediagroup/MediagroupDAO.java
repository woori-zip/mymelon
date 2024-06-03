package kr.co.mymelon.mediagroup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//@Service
//@Repository 모델클래스 지정, 스프링 컨테이너(톰캣)가 자동으로 객체 생성해 준다
@Repository
public class MediagroupDAO {
   
   public MediagroupDAO(){
      System.out.println("----MediagroupDAO() 객체 생성됨");   
   }//end
   
   private StringBuilder sql = null;
   
   //@Autowired 스프링 컨테이너가 생성해 준 객체를 연결
   @Autowired
   private JdbcTemplate jt; //application.properties에서 DB연결에 관련된 정보를 가져온ㄷ
                      //DBOpen dbopen = new DBOpen와 동일한 형태
                      //에러 JdbcTemplate jt = new JdbcTemplate()
   
   
   
   public int create(MediagroupDTO mediagroupDto) {
      int cnt = 0;
      try {
         sql = new StringBuilder();
         
         sql.append(" INSERT INTO mediagroup(mediagroupno, title) ");
         sql.append(" VALUES(mediagroup_seq.nextval,?) ");
         //Maria DB
         //INSERT INTO mediagroup(title) VALUES(?);
         
         //SQL문 (insert, update, delete) 실행
         cnt = jt.update(sql.toString(), mediagroupDto.getTitle());
      }catch (Exception e) {
         System.err.println("미디어그룹등록실패:" + e);
      }//end
      return cnt;
   }//create() end
   
   
   
   //참조 basic01_java프로젝트 oop0325.Test06_anonymous 클래스 참조
   public int mediagroupInsert(final MediagroupDTO mediagroupDto) {
      int result = 0;
      final String sql =" INSERT INTO mediagroup(mediagroupno, title) VALUES(mediagroup_seq.nextval,?)";
      //Maria DB
      //INSERT INTO mediagroup(title) VALUES(?);
      
      result = jt.update(sql, new PreparedStatementSetter() {
         
         @Override
         public void setValues(PreparedStatement ps) throws SQLException {
            ps.setString(1, mediagroupDto.getTitle());
            
         }
      });
      return result;
   }//mediagroupInsert() end
   
   
   
   public List<MediagroupDTO> list(){
      List<MediagroupDTO> list = null;
      
      try {
         
         sql = new StringBuilder();
         sql.append(" SELECT mediagroupno, title ");
         sql.append(" FROM mediagroup ");
         sql.append(" ORDER BY mediagroupno DESC ");
         
         
         RowMapper<MediagroupDTO> rowMapper = new RowMapper<MediagroupDTO>() {
            
            @Override
            public MediagroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
               MediagroupDTO mediagroDto = new MediagroupDTO();
               mediagroDto.setMediagroupno(rs.getInt("mediagroupno"));
               mediagroDto.setTitle(rs.getString("title"));
               return mediagroDto;
            }//mapRow() end
         };//rowMapper 익명 내부 객체
         
         list = jt.query(sql.toString(), rowMapper);
         
      }catch (Exception e) {
         System.out.println("미디어그룹목록실패:" + e);
      }//end
      return list;
   }//list() end
   
   
   
   public int totalRowCount() {
      int cnt = 0;
      try {
         sql = new StringBuilder();
         sql.append(" SELECT COUNT(*) FROM mediagroup ");
         //SELECT를 실행했을 때 하나의 객체(Object) 결과 값이 나올 때 사용
         cnt = jt.queryForObject(sql.toString(), Integer.class);
      }catch (Exception e) {
         System.out.println("전체행개수조회실패:" + e);
      }//end
      return cnt;
   }//totalRowCount() end
   
   
   
   public String title() {
      String title = "";
      try {
         sql = new StringBuilder();
         sql.append(" SELECT title FROM mediagroup WHERE mediagroupno=46 ");
         title = jt.queryForObject(sql.toString(), String.class);
         
      }catch (Exception e) {
         System.out.println("미디어그룹목록실패:" + e);
      }//end
      return title;
   }//title() end
   
   
   
   public int delete(int mediagroupno) {
	      int cnt = 0;
	      try {
	         sql = new StringBuilder();
	         sql.append(" DELETE FROM mediagroup ");
	         sql.append(" WHERE mediagroupno = ? ");
	         cnt = jt.update(sql.toString(), mediagroupno);
	      }catch (Exception e) {
	         System.out.println("미디어그룹삭제실패:" + e);
	      }//end
	      return cnt;
	   }//totalRowCount() end
   
   
   
   public MediagroupDTO read(int mediagroupno){
	   MediagroupDTO mediagroupDto = null;
	      
	      try {
	         
	         sql = new StringBuilder();
	         sql.append(" SELECT mediagroupno, title ");
	         sql.append(" FROM mediagroup ");
	         sql.append(" WHERE mediagroupno = " + mediagroupno);
	         
	         RowMapper<MediagroupDTO> rowMapper = new RowMapper<MediagroupDTO>() {
	            
	            @Override
	            public MediagroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	               MediagroupDTO mgDto = new MediagroupDTO();
	               mgDto.setMediagroupno(rs.getInt("mediagroupno"));
	               mgDto.setTitle(rs.getString("title"));
	               return mgDto;
	            }//mapRow() end
	         };//rowMapper 익명 내부 객체
	         
	         mediagroupDto = jt.queryForObject(sql.toString(), rowMapper);
	         
	      }catch (Exception e) {
	         System.out.println("미디어그룹목록실패:" + e);
	      }//end
	      return mediagroupDto;
	   }//list() end
   
   
   
	public int update(MediagroupDTO mediagroupDto) {
		int cnt=0;
		try {
	         sql = new StringBuilder();
	         sql.append(" UPDATE mediagroup ");
	         sql.append(" SET title = ? ");
	         sql.append(" WHERE mediagroupno = ? ");
	         
	         cnt = jt.update(sql.toString(), new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, mediagroupDto.getTitle());
					ps.setInt(2, mediagroupDto.getMediagroupno());
				}
			});
		} catch (Exception e) {
			System.out.println("미디어그룹수정실패 : "+e);
		}//end
		return cnt;
	}//update() end
	
	
	
	public List<MediagroupDTO> list2(int start, int end){//페이징
	      List<MediagroupDTO> list = null;
	      
	      try {
	         
	         sql = new StringBuilder();
	         sql.append(" SELECT AA.* ");
	         sql.append(" FROM( ");
	         sql.append(" 		SELECT ROWNUM as RNUM, BB.* ");
	         sql.append(" 		FROM( ");
	         sql.append(" 			SELECT mediagroupno, title ");
	         sql.append(" 			FROM mediagroup ");
	         sql.append(" 			ORDER BY mediagroupno DESC ");
	         sql.append(" 			)BB ");
	         sql.append(" 		)AA ");
	         sql.append(" WHERE AA.RNUM >= " + start + " AND AA.RNUM <= " + end);
	         
	         RowMapper<MediagroupDTO> rowMapper = new RowMapper<MediagroupDTO>() {
	            
	            @Override
	            public MediagroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	               MediagroupDTO mediagroDto = new MediagroupDTO();
	               mediagroDto.setMediagroupno(rs.getInt("mediagroupno"));
	               mediagroDto.setTitle(rs.getString("title"));
	               return mediagroDto;
	            }//mapRow() end
	         };//rowMapper 익명 내부 객체
	         
	         list = jt.query(sql.toString(), rowMapper);
	         
	      }catch (Exception e) {
	         System.out.println("미디어그룹페이징실패:" + e);
	      }//end
	      return list;
	   }//list2() end
}//class end