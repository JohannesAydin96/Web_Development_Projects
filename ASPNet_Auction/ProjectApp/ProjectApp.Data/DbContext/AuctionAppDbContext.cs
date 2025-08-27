using Microsoft.EntityFrameworkCore;
using ProjectApp.Data.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Data.DbContext
{
    public class AuctionAppDbContext : Microsoft.EntityFrameworkCore.DbContext
    {
        public DbSet<Auction> tblAuctions { get; set; }
        public DbSet<Bid> tblBids { get; set; }

        public AuctionAppDbContext(DbContextOptions<AuctionAppDbContext> options)
        : base(options) { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<Auction>()
                .HasMany(a => a.Bids)
                .WithOne(b => b.Auction)
                .HasForeignKey(b => b.AuctionId);
        }
    }
}
