package kr.co.mymelon.media;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.mymelon.mediagroup.MediagroupDTO;
import net.utility.Utility;

@Controller
public class MediaCont {
   
   @Autowired //mediaDAO 끌어옴
   MediaDAO mediaDao;
   
   public MediaCont() {
      System.out.println("-----MediaCont()객체 생성됨");
   }//end
   
   @RequestMapping("media/list.do")
   public ModelAndView list(int mediagroupno) { //부모 글 번호
      ModelAndView mav = new ModelAndView();
      mav.setViewName("media/list");
      mav.addObject("list",mediaDao.list(mediagroupno));
      mav.addObject("mediagroupno", mediagroupno);
      return mav;
      
   }//list() end
   
   @GetMapping("media/create.do")
   public ModelAndView createForm(int mediagroupno) {
      ModelAndView mav = new ModelAndView();
      mav.setViewName("media/createForm");
      mav.addObject("mediagroupno", mediagroupno);
      return mav;
      
   }//createForm() end
   
   @PostMapping("media/create.do")
   public ModelAndView createProc(@ModelAttribute MediaDTO mediaDto, HttpServletRequest req) {
   //public ModelAndView createProc(String title, MultipartFile posterMF, MultipartFile filenameMF) {
      
      ModelAndView mav = new ModelAndView();
      mav.setViewName("media/msgView");
      //////////////////////////////////////////////////
      /*
      MultipartFile posterMF = mediaDto.getPosterMF();
      MultipartFile filenameMF = mediaDto.getFilenameMF();
      System.out.println(posterMF.getOriginalFilename());
      System.out.println(posterMF.getSize());
      System.out.println(filenameMF.getOriginalFilename());
      System.out.println(filenameMF.getSize());
      */
      
      //첨부된 파일 처리
      //->실제 파일은 /storage 폴더에 저장
      //->저장된 파일 관련 정보는 media 테이블에 저장
      
      //파일 저장 폴더의 실제 물리적인 경로 가져오기
      ServletContext application = req.getServletContext();
      String basePath = application.getRealPath("/storage");
      
      //파일명 리네임 패턴
      //->sky_1.png
      //->sky_2.png
      //->sky_3.png
      
      //1)<input type="file" name="posterMF">
      String poster = "-";
      MultipartFile posterMF = mediaDto.getPosterMF();   //파일 가져오기      
      if(posterMF != null && !posterMF.isEmpty()) {      //파일이 정상적으로 첨부되었다면
         try {
            String o_poster = posterMF.getOriginalFilename();
            poster = o_poster;
            
            File file = new File(basePath, o_poster); //파일클래스에 해당파일 담기
            int i = 1;
            while(file.exists()) { //파일이 존재한다면
               int lastDot = o_poster.lastIndexOf(".");
               poster = o_poster.substring(0, lastDot) + "_" + i + o_poster.substring(lastDot); //sky_1.png
               file = new File(basePath, poster);
               i++;
            }//while end
            
            posterMF.transferTo(file); //파일 저장
            
         }catch (Exception e) {
            System.out.println(e);
         }//try end
      }//if end
      
      mediaDto.setPoster(poster);
      
      //2)<input type="file" name="filenameMF">
      String filename = "-";
      long filesize = 0;
      MultipartFile filenameMF = mediaDto.getFilenameMF();   //파일 가져오기      
      if(filenameMF != null && !filenameMF.isEmpty()) {      //파일이 정상적으로 첨부되었다면
         try {
            String o_filename = filenameMF.getOriginalFilename();
            filename  = o_filename;
            
            File file = new File(basePath, o_filename); //파일클래스에 해당파일 담기
            int i = 1;
            while(file.exists()) { //파일이 존재한다면
               int lastDot = o_filename.lastIndexOf(".");
               filename = o_filename.substring(0, lastDot) + "_" + i + o_filename.substring(lastDot); //sky_1.png
               file = new File(basePath, filename);
               i++;
            }//while end
            
            filenameMF.transferTo(file); //파일 저장
            
         }catch (Exception e) {
            System.out.println(e);
         }//try end
      }//if end
      
      mediaDto.setFilename(filename);
      mediaDto.setFilesize(filenameMF.getSize());
      //////////////////////////////////////////////////
      
      
      //파일 정보 테이블에 저장하기
      int cnt = mediaDao.create(mediaDto);
      if(cnt == 0) {
         
         String msg1 = "<p>음원 등록 실패</p>";
         String img = "<img src='../images/fail.png'>";
         String link1 = "<input type='button' value='다시시도' onclick='javascipt:history.back()'>";
         String link2 = "<input type='button' value='목록으로' onclick='location.href=\"list.do?mediagroupno=" + mediaDto.getMediagroupno() + "\"'>";
         
         mav.addObject("msg1", msg1);
         mav.addObject("img", img);
         mav.addObject("link1", link1);
         mav.addObject("link2", link2);
         
      }else{
         
         String msg1 = "<p>음원 등록 성공</p>";
         String img = "<img src='../images/sound.png'>";
         String link2 ="<input type='button' value='목록으로' onclick='location.href=\"list.do?mediagroupno=" + mediaDto.getMediagroupno() + "\"'>";
         
         mav.addObject("msg1", msg1);
         mav.addObject("img", img);
         mav.addObject("link2", link2);
         
      }//if end
      
      return mav;
   }//createProc() end
	
   
   
   @GetMapping("media/delete.do")
	public ModelAndView deleteForm(int mediano) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/deleteForm");
		mav.addObject("mediano",mediano); //삭제할 글번호
		return mav;
	}//deleteForm() end

	@PostMapping("media/delete.do")
	public ModelAndView deleteProc(int mediano, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/msgView");
		
		//삭제하고자 하는 글정보 가져오기(/storge 폴더에서 삭제할 파일명 확인 위함)
		MediaDTO oldDTO = mediaDao.read(mediano);
		
		//폴더 경로 basePath 가져오기
		ServletContext application = req.getServletContext();
		String basePath = application.getRealPath("/storage");
  
		int cnt=mediaDao.delete(mediano);
		if(cnt==0) {			
			// DB에서 행 삭제 실패
			String msg1="<p>음원 삭제 실패</p>";
			String img ="<img src='../images/fail.png'>";
			String link1 ="<input type='button' value='다시시도' onclick='javascript:history.back()'>";
			String link2 ="<input type='button' value='그룹목록' onclick=\"location.href='list.do'>";
			
			mav.addObject("msg1",  msg1);
			mav.addObject("img",   img);
			mav.addObject("link1", link1);
			mav.addObject("link2", link2);
		}else {
			// DB에서 행 삭제 성공
			String msg1="<p>음원 삭제 성공</p>";
			String img ="<img src='../images/sound.png'>";
	        String link2 = "<input type='button' value='음원목록' onclick='location.href=\"list.do?mediagroupno=" + oldDTO.getMediagroupno() + "\"'>";
			
			mav.addObject("msg1",  msg1);
			mav.addObject("img",   img);
			mav.addObject("link2", link2);
			
			//첨부파일 삭제
            Utility.deleteFile(basePath, oldDTO.getPoster());   //기존 파일 삭제
            Utility.deleteFile(basePath, oldDTO.getFilename()); //기존 파일 삭제
            
            /*
            String basePath = req.getRealPath("/storage");
            UploadSaveManager.deleteFile(basePath, oldDTO.getPoster());
            UploadSaveManager.deleteFile(basePath, oldDTO.getFilename());
            */
	    }
	    return mav;
	}//deleteProc() end
	
	
	
	@GetMapping("media/update.do")
	public ModelAndView updateForm(int mediano) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("media/updateForm");
		MediaDTO mediaDto = mediaDao.read(mediano); //수정하고자 하는 행을 가져오기
		mav.addObject("mediaDto",mediaDto);
		return mav;
	}//updateForm() end
	
	@PostMapping("media/update.do")
	   //public ModelAndView updateProc(int mediagroupno, int mediano, String title, MultipartFile posterMF, MultipartFile filenameMF) {
	   public ModelAndView updateProc(@ModelAttribute MediaDTO mediaDto, HttpServletRequest req) {
	   
	      //기존에 저장된 정보를 가져오기
	      MediaDTO oldDTO = mediaDao.read(mediaDto.getMediano());
	      
	      ////////////////////////////////////////////////////////////////////////////////////
	      
	      //파일을 수정할 것인지?
	      ServletContext application = req.getServletContext();
	      String basePath = application.getRealPath("/storage");
	      
	      MultipartFile posterMF = mediaDto.getPosterMF();
	      if(posterMF.getSize()>0 && posterMF != null && !posterMF.isEmpty()) {   
	         //새로운 포스터 파일이 첨부되어 전송되었는지??  && 파일이 정상적으로 첨부되었다면 
	         //posterMF.getSize()>0 파일이 존재한다!!!
	         try {
	            String poster = "-";
	            String o_poster = posterMF.getOriginalFilename();
	            
	            
	            poster = o_poster;
	               
	            File file = new File(basePath, o_poster); //파일클래스에 해당파일 담기
	            int i = 1;
	            while(file.exists()) { //파일이 존재한다면
	               int lastDot = o_poster.lastIndexOf(".");
	               poster = o_poster.substring(0, lastDot) + "_" + i + o_poster.substring(lastDot); //sky_1.png
	               file = new File(basePath, poster);
	               i++;
	            }//while end
	            

	            posterMF.transferTo(file);            //신규 파일 저장
	            Utility.deleteFile(basePath, oldDTO.getPoster()); //기존 파일 삭제
	               
	            mediaDto.setPoster(o_poster);
	            
	            }catch(Exception e) {
	               System.out.println(e);
	            }//try end
	         
	      }else {
	         //포스터 파일은 수정하지 않은 경우
	         mediaDto.setPoster(oldDTO.getPoster()); //기존에 저장된 파일명
	      }//if end
	      
	      
	      //2)
	      MultipartFile filenameMF = mediaDto.getFilenameMF();
	      if(filenameMF.getSize()>0 && filenameMF !=null && !filenameMF.isEmpty()) {
	         try {
	         String filename = "-";
	         long filesize =0;
	         String o_filename = filenameMF.getOriginalFilename();
	         filename = o_filename;
	         
	         File file = new File(basePath, o_filename); //파일클래스에 해당파일 담기
	         int i = 1;
	         while(file.exists()) { //파일이 존재한다면
	            int lastDot = o_filename.lastIndexOf(".");
	            filename = o_filename.substring(0, lastDot) + "_" + i + o_filename.substring(lastDot); //sky_1.png
	            file = new File(basePath, filename);
	            i++;
	         }//while end
	             
	         filenameMF.transferTo(file);           // 신규 파일 저장
	         Utility.deleteFile(basePath, oldDTO.getFilename()); //기존 파일 삭제
	         
	         mediaDto.setFilename(filename);
	         mediaDto.setFilesize(filenameMF.getSize());
	         
	         }catch(Exception e) {
	            System.out.println(e);
	         }//try end
	      
	      }else {
	         mediaDto.setFilename(oldDTO.getFilename());
	         mediaDto.setFilesize(oldDTO.getFilesize());
	      }//if end
	      
	      ////////////////////////////////////////////////////////////////////////
	      
	      ModelAndView mav = new ModelAndView();
	      int cnt = mediaDao.update(mediaDto);
	      if(cnt==0) {
	         mav.setViewName("media/msgView");
	         String msg1 = "<p>음원 수정 실패</p>";
	         String img  = "<img src='../images/fail.png'>";
	         String link1 = "<input type='button' value='다시시도' onclick='javascript:history.back()'>";
	         String link2 = "<input type='button' value='음원목록' onclick='location.href=\"list.do?mediagroupno=" + oldDTO.getMediagroupno() + "\"'>";
	         
	         mav.addObject("msg1", msg1);
	         mav.addObject("img", img);
	         mav.addObject("link1", link1);
	         mav.addObject("link2", link2);
	         
	      }else {
	      
	         mav.setViewName("redirect:/media/list.do?mediagroupno=" + oldDTO.getMediagroupno());
	         
	      }//if end
	      
	      return mav;
	   }//updateProc() end
	
	
	
	@GetMapping("media/read.do")
	public ModelAndView read(int mediano) {
		ModelAndView mav = new ModelAndView();
		MediaDTO mediaDto = mediaDao.read(mediano);
		if(mediaDto != null) {
			String filename = mediaDto.getFilename();
			filename = filename.toLowerCase();
			if(filename.endsWith(".mp3")) {
				mav.setViewName("media/readMP3");
			} else if(filename.endsWith(".mp4")) {
				mav.setViewName("media/readMP4");
			}//if end
		}//if end
		
		mav.addObject("mediaDto", mediaDto);
		
		return mav;
	}//read() end
   
   
}//class end