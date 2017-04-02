Shows different strategies to *structure a web application's view layer* based on the spring-mvc, spring-webflow, freemarker and tiles frameworks.

This demo has *three flows* doing the same thing, implemented according to different strategies:

**Bad macros**: Using many unclear macros each rendering not well-formed markup, depending on each other to make the markup well-formed.

**Better macros**: Still using many macros, but each macro renders nested content so that it produces well-formed markup. Each page still redefines its layout which is the same for many pages.

**Tiles**: Separates the responsibilities of layout and defining the view components. This eliminates all layout-macros and leads to clear, small and reusable components. At the cost of an extra tiles layout definition for each page.

Running the web application requires Java 5 and maven.

Run: `mvn jetty:run`

Then point browser at <http://localhost:8080>
