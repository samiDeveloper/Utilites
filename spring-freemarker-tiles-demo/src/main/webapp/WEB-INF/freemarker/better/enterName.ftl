<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#import "better-macros.ftl" as bettermacros />

<@bettermacros.contentcontainer title="Better flow - enter name">

<@form.form modelAttribute="mainBean">
    <div>name: <@form.input id="name" path="name" /></div>
    <div><input name="_eventId_next" type="submit" value="next"/>
</@form.form>

</@bettermacros.contentcontainer>
