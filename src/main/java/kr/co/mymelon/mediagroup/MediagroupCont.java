package kr.co.mymelon.mediagroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MediagroupCont {
	public MediagroupCont() {
		System.out.println("----MediagroupCont() 객체 생성됨");
	}//end
	
	
	
	//@Autowired 스프링컨테이너(톰캣)가 생성해 준 객체를 연결
	@Autowired
	private MediagroupDAO mediagroupDao;
	//private MediagroupDAO dao = new mediagropDAO;
	//@Repository에 의해서 이미 객체가 생성되어 있으므로 new하지 않아도 된다
	
	
	
	//결과확인 http://localhost:9095/mediagroup/list.do
	
	/* 1) 페이징 없는 list
	@RequestMapping("mediagroup/list.do")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mediagroup/list_v1");
		
		List<MediagroupDTO> list = mediagroupDao.list();	//전체 목록
		int totalRowCount = mediagroupDao.totalRowCount();	//총 글 개수
		
		mav.addObject("list", list);
		mav.addObject("count", totalRowCount);
		
		return mav;
	}//list() end
	*/
	
	// 2) 페이징 있는 list
	@RequestMapping("mediagroup/list.do")
	public ModelAndView list(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mediagroup/list");
		
		int totalRowCount = mediagroupDao.totalRowCount(); //총 글 개수
		
		//페이징 설정
		int numPerPage = 5; //한 페이지당 표시할 레코드 개수
		int pagePerBlock = 5; //블록 당 표시할 페이지 수
		
		String PageNum = req.getParameter("pageNum"); //현재 페이지 번호 가져오기
		if(PageNum==null) {
			PageNum="1"; //기본값 설정
		}
		
		int currentPage = Integer.parseInt(PageNum); //현재 페이지 번호
		int startRow	= (currentPage-1)*numPerPage+1; //현재 페이지의 첫 번째 레코드 번호
		int endRow		= currentPage*numPerPage; //현재 페이지의 마지막 레코드 번호
		
		//전체 페이지 수 계산
		double totcnt	= (double)totalRowCount/numPerPage; //전체 페이지 수 계산
		int totalPage	= (int)Math.ceil(totcnt); //전체 페이지 수. 올림하여 계산
		
		//현재 블록 계산
		double d_page	= (double)currentPage/pagePerBlock; //현재 페이지가 몇 번째 블록에 속하는지 계산
		int Pages		= (int)Math.ceil(d_page)-1; //현재 블록 번호
		int startPage	= Pages*pagePerBlock; //현재 블록의 시작 페이지 번호
		int endPage		= startPage+pagePerBlock+1; //현재 블록의 마지막 페이지 번호
		
		List list=null;
		if(totalRowCount>0) {
			list = mediagroupDao.list2(startRow, endRow); //현재 페이지의 데이터 리스트
		}else {
			list = Collections.EMPTY_LIST; //데이터가 없는 경우 비어 있는 리스트
		}//if end
		
		mav.addObject("pageNum", currentPage); //현재 페이지 번호
		mav.addObject("count", totalRowCount); //전체 레코드 수
		mav.addObject("totalPage", totalPage); //전체 페이지 수
		mav.addObject("startPage", startPage); //현재 블록의 시작 페이지
		mav.addObject("endPage", endPage); //현재 블록의 마지막 페이지
		mav.addObject("list", list); //페이지에 따른 데이터 리스트
		
		return mav;
	}//list() end
	
	
	//미디어 그룹 쓰기 페이지 호출
	@GetMapping("mediagroup/create.do")
	public String createForm() {
		return "mediagroup/createForm"; // /WEB-INF/views/mediagroup/createForm.jsp
	}//createForm() end
	
	
	
	@PostMapping("mediagroup/create.do")
	public ModelAndView creatProc(@ModelAttribute MediagroupDTO mediagroupDto) {
		ModelAndView mav = new ModelAndView();
		
		//int cnt = mediagroupDao.create(mediagroupDto);
		int cnt = mediagroupDao.mediagroupInsert(mediagroupDto);
		
		if(cnt==0) {
			mav.setViewName("mediagroup/msgView");	
			String msg1="<p>미디어 그룹 등록 실패</p>";
			String img ="<img src='../images/fail.png'>";
			String link1 ="<input type='button' value='다시시도' onclick='javascript:history.back()'>";
			String link2 ="<input type='button' value='그룹목록' onclick=\"location.href='list.do'>";
			
			mav.addObject("msg1",  msg1);
			mav.addObject("img",   img);
			mav.addObject("link1", link1);
			mav.addObject("link2", link2);
		}else {
			mav.setViewName("redirect:/mediagroup/list.do");
		}//if end
		return mav;
	}//createProc() end
	
	
	
	//list.jsp에서 ?mediagroupno 값을 가져오는 다양한 방법
	//<input type="button" value="삭제" onclick="location.href='delete.do?mediagroupno=${mediagroupDto.mediagroupno}'">
	//맞고 틀린 게 아니라, 다양한 방법들 중 하나일 뿐임
	@GetMapping("mediagroup/delete.do")
	/*
	//1) HttpServletRequest req
	public ModelAndView deleteForm(HttpServletRequest req) {
		int mediagroupno = Integer.parseInt(req.getParameter("mediagroupno"));
	}//deleteForm() end
	
	
	//2) @ModelAttribute MediagroupDTO mediagroupDto
	public ModelAndView deleteForm(@ModelAttribute MediagroupDTO mediagroupDto) {
		int mediagroup = mediagroupDto.getMediagroupno();
	}
	
	
	//3) @RequestParam("mediagroup") int a
	public ModelAndView deleteForm(@RequestParam("mediagroup") int mediagroupno) {}
	*/
	//4) int mediagroup
	public ModelAndView deleteForm(int mediagroupno) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mediagroup/deleteForm");
		mav.addObject("mediagroupno",mediagroupno);
		
		return mav;
	}//deleteForm() end
	
	
	@PostMapping("mediagroup/delete.do")
	public ModelAndView deleteProc(int mediagroupno) {
		ModelAndView mav = new ModelAndView();
		int cnt = mediagroupDao.delete(mediagroupno);
		if(cnt==0) {
			mav.setViewName("mediagroup/msgView");	
			
			String msg1="<p>미디어 그룹 삭제 실패</p>";
			String img ="<img src='../images/fail.png'>";
			String link1 ="<input type='button' value='다시시도' onclick='javascript:history.back()'>";
			String link2 ="<input type='button' value='그룹목록' onclick=\"location.href='list.do'>";
			
			mav.addObject("msg1",  msg1);
			mav.addObject("img",   img);
			mav.addObject("link1", link1);
			mav.addObject("link2", link2);
		}else {
			mav.setViewName("redirect:/mediagroup/list.do");
		}//if end
		return mav;
	}//deleteProc() end
	
	@GetMapping("mediagroup/update.do")
	public ModelAndView updateForm(int mediagroupno) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mediagroup/updateForm");
		mav.addObject("mediagroupDto", mediagroupDao.read(mediagroupno));
		return mav;
	}//deleteProc() end
	
	@PostMapping("mediagroup/update.do")
	public ModelAndView updateProc(MediagroupDTO mediagroupDto) {
		ModelAndView mav = new ModelAndView();
		int cnt = mediagroupDao.update(mediagroupDto);
		if(cnt==0) {
			mav.setViewName("mediagroup/msgView");	
			
			String msg1="<p>미디어 그룹 수정 실패</p>";
			String img ="<img src='../images/fail.png'>";
			String link1 ="<input type='button' value='다시시도' onclick='javascript:history.back()'>";
			String link2 ="<input type='button' value='그룹목록' onclick=\"location.href='list.do'>";
			
			mav.addObject("msg1",  msg1);
			mav.addObject("img",   img);
			mav.addObject("link1", link1);
			mav.addObject("link2", link2);
		}else {
			mav.setViewName("redirect:/mediagroup/list.do");
		}//if end
		return mav;
	}//updateProc() end
}//class end
