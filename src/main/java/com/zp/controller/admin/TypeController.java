package com.zp.controller.admin;

import com.zp.pojo.Type;
import com.zp.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        Type typename = typeService.getTypeByName(type.getName());
        if (typename!=null){
            result.rejectValue("name","nameError","不能重复添加的分类");
        }
        if (result.hasErrors()){
            return "admin/types-input";

        }

        Type t = typeService.saveType(type);
        if (t==null){
            attributes.addFlashAttribute("message","添加失败");
        }else {
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    /*BindingResult result: 返回的提示信息类，必须放在要返回的类的对象的后面*/
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id ,RedirectAttributes attributes){
        Type typename = typeService.getTypeByName(type.getName());
        if (typename!=null){
            result.rejectValue("name","nameError","不能重复添加的分类");
        }
        if (result.hasErrors()){
            return "admin/types-input";

        }

        Type t = typeService.updateType(id,type);
        if (t==null){
            attributes.addFlashAttribute("message","修改失败");
        }else {
            attributes.addFlashAttribute("message","修改成功");
        }
        return "redirect:/admin/types";
    }
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id){
        typeService.deleteType(id);
        return "redirect:/admin/types";
    }
}
