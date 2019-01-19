package com.sample.springwebboard.controller

import com.sample.springwebboard.model.entity.MessageEntity
import com.sample.springwebboard.model.entity.MessageForm
import com.sample.springwebboard.model.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class Controller {
    @Autowired
    lateinit var service: MessageService

    @GetMapping("/")
    fun getMessage(mv: ModelAndView): ModelAndView {

        val list: List<MessageEntity> = service.getMessage()

        mv.setViewName("web-board")
        mv.addObject("list", list)

        return mv
    }

    @PostMapping("/")
    fun postMessage(@ModelAttribute form: MessageForm): ModelAndView {

        service.postMessage(form.name, form.text)

        return ModelAndView("redirect:/")
    }
}