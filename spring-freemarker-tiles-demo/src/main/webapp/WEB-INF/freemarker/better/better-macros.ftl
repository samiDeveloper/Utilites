<#macro contentcontainer title>
    <html>
    <head>
    <title>${title}</title>
    </head>
    <body>
    
    <@bettermacros.header />

    <h2>${title}</h2>

    <#nested>
    
    <@bettermacros.footer />

    </body>
    </html>
</#macro>

<#macro header>
    <p><a href="/">Home</a> | Main menu...</p>
</#macro>

<#macro footer>
    <p>Bla bla footer</p>
</#macro>
