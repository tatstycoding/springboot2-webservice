package me.yisak.web;

import lombok.RequiredArgsConstructor;
import me.yisak.config.auth.LoginUser;
import me.yisak.config.auth.dto.SessionUser;
import me.yisak.service.posts.PostsService;
import me.yisak.web.dto.PostsResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
// 페이지에 관련된 컨트롤러는 모두 IndexController를 사용

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
       /*      @LoginUser SessionUser user
        *  1. 기존에 (User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선됨.
        *  2. 이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됨.
        * */

        model.addAttribute("posts", postsService.findAllDesc());
        // Model 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있습니다.
        // 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달합니다.
        // "posts"는 index.mustache 의  {{#posts}} 로 전달된것.

     //   SessionUser user = (SessionUser) httpSession.getAttribute("user");
       /*   (SessionUser) httpSession.getAttribute("user")
        * 1. 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성.
        * 2. 즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있다.
        * 3. 하지만 @LoginUser 어노테이션으로 생략됨.
        */

        if (user != null){
       /*   if (user != null)
        * 1.세션에 저장된 값이 있을 때만 model에 userName으로 등록
        * 2. 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 됨.
      * */

            model.addAttribute("userName", user.getName());
        }
        return "index"; //musctache starter 덕분에 Controller에서 문자열을 반환할때 앞의 경로와 뒤의 파일 확장자가 자동으로 확정됨.
                        // 즉 앞에는 -> src/main/resources/templates로
                        //    뒤에는 -> .mustache 가 붙는다
                        // 최종적으로 src/main/resources/templates/index.mustache가 되어 View Reseolve가 처리하게 됨.
    }

    @GetMapping("/posts/save") // 인덱스 글 등록 버튼 // 게시글 등록 페이지로 감.
    public String  postsSave(){
        return "posts-save"; // posts-save.mustache 파일 호출. // 게시글 등록 페이지.
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        // Model 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있습니다.
        // 여기서는 postsService.findById(id)로 가져온 결과를 PostsResponseDto타입 변수dto에 담아서
        // post로 posts-update.mustache에 전달합니다.
        // "post"는 posts-update.mustache 의  value="{{post.~~~~}} 로 전달된것.

        return "posts-update"; // posts-update.mustache 파일 호출
    }




}
