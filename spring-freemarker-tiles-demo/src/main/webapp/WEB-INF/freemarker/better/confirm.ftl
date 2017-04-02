<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#import "better-macros.ftl" as bettermacros />

<@bettermacros.contentcontainer title="Better flow - confirm">

<@form.form modelAttribute="mainBean">
    <div>name: ${mainBean.name}</div>
    <div><input name="_eventId_next" type="submit" value="ok"/>
</@form.form>

</@bettermacros.contentcontainer>
