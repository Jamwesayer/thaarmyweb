USE [Test_Signaal_Database]
GO

/****** Object:  Table [dbo].[UserActive]    Script Date: 4-6-2018 09:38:15 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[UserActive](
	[UserId] [nchar](15) NOT NULL,
	[Datum] [date] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[UserId] ASC,
	[Datum] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[UserActive] ADD  DEFAULT (getdate()) FOR [Datum]
GO

