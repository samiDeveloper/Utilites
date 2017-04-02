<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>

<@form.form modelAttribute="mainBean">
    <div>name: <@form.input id="name" path="name" /></div>
    <div><input name="_eventId_next" type="submit" value="next"/>
</@form.form>
