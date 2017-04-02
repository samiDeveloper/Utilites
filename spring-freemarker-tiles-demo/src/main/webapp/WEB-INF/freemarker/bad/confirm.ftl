<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#import "bad-macros.ftl" as badmacros />

<#assign title="Bad flow - confirm" />
<@badmacros.openbody title=title/>

<@badmacros.header />

<h2>${title}</h2>

<@form.form modelAttribute="mainBean">
    <div>name: ${mainBean.name}</div>
    <div><input name="_eventId_next" type="submit" value="ok"/>
</@form.form>

<@badmacros.footer />

<@badmacros.closebody />
