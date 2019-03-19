(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['itemList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "            <tr>\n            <td>\n            <p id="
    + alias2(alias1((depth0 != null ? depth0.itemId : depth0), depth0))
    + "class=\"itemList-scroll\">"
    + alias2(alias1((depth0 != null ? depth0.itemDescription : depth0), depth0))
    + "</p>\n            </td>\n            </tr>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "\n    <div class=\"panel-body\">\n    <table>\n        <thead>\n            <th>\n            Items\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n        </thead>\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n    </div>\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['itemInfo.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "            <tr>\n            <td>\n            <p id="
    + alias2(alias1((depth0 != null ? depth0.itemId : depth0), depth0))
    + "class=\"itemList-scroll\">"
    + alias2(alias1((depth0 != null ? depth0.itemDescription : depth0), depth0))
    + "</p>\n            </td>\n            </tr>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "\n    <div class=\"panel-body\">\n    <table>\n        <thead>\n            <th>\n            Items\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n            <th>\n            </th>\n        </thead>\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n    </div>\n";
},"useData":true});
})();
