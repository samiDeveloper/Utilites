<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#import "bad-macros.ftl" as badmacros />

<#assign title="Bad flow - enter name" />
<@badmacros.openbody title=title/>

<@badmacros.header />

<h2>${title}</h2>

<@form.form modelAttribute="mainBean">
    <div>name: <@form.input id="name" path="name" /></div>
    <div><input name="_eventId_next" type="submit" value="next"/>
</@form.form>

<@badmacros.footer />

<@badmacros.closebody />
