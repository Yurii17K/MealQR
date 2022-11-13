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
        "img" : "data:image/jpeg;base64,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",
        "desc" : "MMM nice."
      },
      {
        "name" : "Drink",
        "price" : "1.79",
        "img" : "https://www.theharvestkitchen.com/wp-content/uploads/2020/06/hydrating-lemon-water-1-scaled-735x1102.jpg",
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
