<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>

<@form.form modelAttribute="mainBean">
    <div>name: ${mainBean.name}</div>
    <div><input name="_eventId_next" type="submit" value="ok"/>
</@form.form>
