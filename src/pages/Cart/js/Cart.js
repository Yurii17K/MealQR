//https://github.com/jasonmoo/t.js interpolating values in an html string for insertion into the DOM via innerHTML
(function(){function c(a){this.t=a}function l(a,b){for(var e=b.split(".");e.length;){if(!(e[0]in a))return!1;a=a[e.shift()]}return a}function d(a,b){return a.replace(h,function(e,a,i,f,c,h,k,m){var f=l(b,f),j="",g;if(!f)return"!"==i?d(c,b):k?d(m,b):"";if(!i)return d(h,b);if("@"==i){e=b._key;a=b._val;for(g in f)f.hasOwnProperty(g)&&(b._key=g,b._val=f[g],j+=d(c,b));b._key=e;b._val=a;return j}}).replace(k,function(a,c,d){return(a=l(b,d))||0===a?"%"==c?(new Option(a)).innerHTML.replace(/"/g,"&quot;"):
a:""})}var h=/\{\{(([@!]?)(.+?))\}\}(([\s\S]+?)(\{\{:\1\}\}([\s\S]+?))?)\{\{\/\1\}\}/g,k=/\{\{([=%])(.+?)\}\}/g;c.prototype.render=function(a){return d(this.t,a)};window.t=c})();


Number.prototype.to_$ = function ()
{
  return "$" + parseFloat( this ).toFixed(2);
};
String.prototype.strip$ = function ()
{
  return this.split("$")[1];
};

var MealQR = 
{

  PromoCode : 0.2,
  Products : [
      {
        "name" : "Food",
        "price" : "21.37",
        "img" : "../samples/lemon.webp"
        "desc" : "MMM nice."
      },
      {
        "name" : "Drink",
        "price" : "1.79",
        "img" : "../samples/lemon.webp",
        "desc" : "Extra money for a slice of lemon."
      }
    ],

  RemoveProduct: function ()
  {
    "use strict";

    var Item = $(this).closest(".shopping-cart--list-item");

    Item.addClass("closing");
	
    window.setTimeout( function () 
	{
      Item.remove();
      MealQR.UpdateTotal();
    }, 500); //animation
  },

  AddProduct: function ()
  {
    "use strict";

    var Quantity = $(this).prev(".product-qty"),
        QuantityF = parseInt(Quantity.html(), 10) + 1;

    MealQR.UpdateSubtotal(this, QuantityF);
  },

  SubtractProduct: function ()
  {
    "use strict";

    var Quantity = $(this).next(".product-qty"),
        Qty = parseInt(Quantity.html(), 10) - 1;
	
	if(Qty <= 0)
	{
		var QuantityF = 0;
	}
	else
	{
		var QuantityF = Qty;
	}

    MealQR.UpdateSubtotal(this, QuantityF);
  },

  UpdateSubtotal: function (Item, Quantity)
  {
    "use strict";

    var MQR = $(Item).closest(".product-modifiers"),
        ProductQuantity = MQR.find(".product-qty"),
        ProductPrice = parseFloat(MQR.data("product-price")),
        Subtotal = MQR.find(".product-total-price"),
        SubtotalF = Quantity * ProductPrice;

    ProductQuantity.html(Quantity);
    Subtotal.html( SubtotalF.to_$() );

    MealQR.UpdateTotal();
  },

  UpdateTotal: function ()
  {
    "use strict";

    var Products = $(".shopping-cart--list-item"),
        Subtotal = 0,
        PromoCode = MealQR.PromoCode;

    for (var i = 0; i < Products.length; i++)
	{
      Subtotal += parseFloat( $(Products[i]).find(".product-total-price").html().strip$() );
    }

    //$("#subtotalCtr").find(".cart-totals-value").html( Subtotal.to_$() );
    //$("#totalCtr").find(".cart-totals-value").html( (Subtotal * PromoCode).to_$() );
	$("#subtotalCtr").find(".cart-totals-value").html( Subtotal.to_$() );
	$("#totalCtr").find(".cart-totals-value").html( (Subtotal - (Subtotal * PromoCode)).to_$() );
  },

  Functions: function ()
  {
    "use strict";

    $(".product-remove").on("click", MealQR.RemoveProduct);
    $(".product-plus").on("click", MealQR.AddProduct);
    $(".product-subtract").on("click", MealQR.SubtractProduct);
  },

  ProductImage: function ()
  {
    "use strict";

    var Images = $(".product-image"),
        MQR,
        img;

    for (var i = 0; i < Images.length; i += 1) {
      MQR = $(Images[i]),
      img = MQR.find(".product-image--img");

      MQR.css("background-image", "url(" + img.attr("src") + ")");
      img.remove();
    }
  },

  RenderTemplates: function ()
  {
    "use strict";

    var Products = MealQR.Products,
        content = [],
        template = new t( $("#shopping-cart--list-item-template").html() );

    for (var i = 0; i < Products.length; i += 1) {
      content[i] = template.render(Products[i]);
    }

    $("#shopping-cart--list").html(content.join(""));
  }

};

MealQR.RenderTemplates();
MealQR.ProductImage();
MealQR.Functions();
