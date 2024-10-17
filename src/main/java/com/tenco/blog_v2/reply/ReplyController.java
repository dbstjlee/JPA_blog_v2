package com.tenco.blog_v2.reply;

import com.tenco.blog_v2.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final ReplyService replyService;
    private final HttpSession session;
    //  HttpSession session 멤버변수 지만 많은 데이터가 덮어씌워짐 --> 동시성 문제 --> 메서드에서 파라미터로 주입 받는게 안정된 상태임


    // 댓글 생성 기능 만들기
    @PostMapping("/reply/save")
    public String save(ReplyDTO.SaveDTO reqDTO) {
        // 로그인 여부 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null) {
            return "redirect:/login-form";
        }

        replyService.saveReply(reqDTO, sessionUser);
        return "redirect:/board/" + reqDTO.getBoardId();
    }

    // 댓글 삭제 기능
//    @DeleteMapping("")
    @PostMapping("/board/{boardId}/reply/{replyId}/delete")
    public String delete(@PathVariable(name = "boardId") Integer boardId, @PathVariable(name = "replyId") Integer replyId) {
        // 삭제도 권한 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null) {
            return "redirect:/login-form";
        }
        replyService.deleteReply(replyId, sessionUser.getId(), boardId);
        return "redirect:/board/" + boardId;
    }



}
